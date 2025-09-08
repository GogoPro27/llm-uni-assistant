import React, { useEffect, useMemo, useState, useRef } from "react";
import { useParams } from "react-router-dom";
import { llmControlAPI, professorGroupSubjectAPI, llmResourceAPI } from "../../../api/endpoints.js";
import Navbar from "../common/Navbar.jsx";
import Header from "../common/Header.jsx";
import Breadcrumbs from "../common/Breadcrumbs.jsx";
import Card from "../common/Card.jsx";
import CardHeader from "../common/CardHeader.jsx";
import Field from "../common/Field.jsx";

function toParamsArray(obj) {
    if (!obj || typeof obj !== "object") return [];
    return Object.entries(obj).map(([k, v]) => ({
        id: crypto?.randomUUID ? crypto.randomUUID() : `${Date.now()}_${Math.random()}`,
        k,
        v: String(v ?? ""),
    }));
}

function toParamsObject(arr) {
    const out = {};
    (arr || []).forEach(({ k, v }) => {
        const key = (k ?? "").trim();
        if (key) out[key] = v;
    });
    return out;
}

export default function EnrollmentDetailsPage() {
    const { professorGroupId } = useParams();

    const [professorGroupSubject, setProfessorGroupSubject] = useState(null);
    const [llmControl, setLlmControl] = useState(null);
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState("");

    const fileInputRef = useRef(null);
    const [fileUploading, setFileUploading] = useState(false);
    const [linkForm, setLinkForm] = useState({ title: "", url: "", description: "" });
    const [linkSaving, setLinkSaving] = useState(false);
    const [resourceOpLoading, setResourceOpLoading] = useState(false);

    const [formData, setFormData] = useState({
        llmProvider: "",
        modelName: "",
        systemPrompt: "",
        strictRag: false,
        relaxedAnswers: false,
        topK: "",
        similarityThreshold: "",
        memoryWindowSize: "",
        params: [],
    });

    useEffect(() => {
        let active = true;
        (async () => {
            try {
                setError("");
                const response = await professorGroupSubjectAPI.findById(professorGroupId);
                console.log("Fetched details:", response);
                if (!active) return;

                // attach resources (defensive: the list endpoint might return {data} or the array directly)
                try {
                    const res = await llmResourceAPI.listByGroup(professorGroupId);
                    const resources = res?.data ?? res;
                    // merge resources into the professorGroupSubject we set
                    const merged = { ...(response || {}), llmResources: resources || [] };
                    setProfessorGroupSubject(merged);
                } catch (resErr) {
                    console.warn("Failed to fetch resources for group:", resErr);
                    // fallback to whatever the response already contains
                    setProfessorGroupSubject(response);
                }

                if (response?.llmControl) {
                    setLlmControl(response.llmControl);
                    setFormData({
                        llmProvider: response.llmControl.llmProvider ?? "",
                        modelName: response.llmControl.modelName ?? "",
                        systemPrompt: response.llmControl.systemPrompt ?? "",
                        strictRag: Boolean(response.llmControl.strictRag ?? false),
                        relaxedAnswers: Boolean(response.llmControl.relaxedAnswers ?? false),
                        topK: Number.isFinite(response.llmControl.topK) ? response.llmControl.topK : "",
                        similarityThreshold: Number.isFinite(response.llmControl.similarityThreshold)
                            ? response.llmControl.similarityThreshold
                            : "",
                        memoryWindowSize: Number.isFinite(response.llmControl.memoryWindowSize)
                            ? response.llmControl.memoryWindowSize
                            : "",
                        params: toParamsArray(response.llmControl.params || {}),
                    });
                }
            } catch (err) {
                console.error("Failed to fetch details:", err);
                if (!active) return;
                setError("Couldn't load this enrollment. Please try again.");
            }
        })();
        return () => {
            active = false;
        };
    }, [professorGroupId]);

    const refreshProfessorGroup = async () => {
        try {
            const fresh = await professorGroupSubjectAPI.findById(professorGroupId);
            // attempt to fetch up-to-date resources and merge
            try {
                const res = await llmResourceAPI.listByGroup(professorGroupId);
                const resources = res?.data ?? res;
                setProfessorGroupSubject({ ...(fresh || {}), llmResources: resources || [] });
            } catch (resErr) {
                console.warn("Failed to refresh resources, using professorGroupSubject payload:", resErr);
                setProfessorGroupSubject(fresh);
            }
        } catch (err) {
            console.error("Failed to refresh group subject:", err);
        }
    };

    const handleUploadClick = () => {
        fileInputRef.current?.click();
    };

    const handleFileSelected = async (e) => {
        const file = e.target.files?.[0];
        if (!file) return;
        setFileUploading(true);
        try {
            await llmResourceAPI.uploadFile(professorGroupId, file);
            await refreshProfessorGroup();
            window.alert("File uploaded.");
        } catch (err) {
            console.error("Upload failed:", err);
            window.alert("Failed to upload file.");
        } finally {
            setFileUploading(false);
            if (fileInputRef.current) fileInputRef.current.value = "";
        }
    };

    const handleDownload = async (resource) => {
        try {
            setResourceOpLoading(true);
            const resp = await llmResourceAPI.download(resource.id);

            let blobCandidate = resp?.data ?? resp;

            if (blobCandidate instanceof ArrayBuffer) {
                blobCandidate = new Blob([blobCandidate], { type: resource.mimeType || 'application/octet-stream' });
            }

            const isBlobLike = blobCandidate && typeof blobCandidate === 'object' && ('size' in blobCandidate || 'arrayBuffer' in blobCandidate || 'type' in blobCandidate);
            if (!(blobCandidate instanceof Blob) && !isBlobLike) {
                throw new Error('Server did not return a file blob.');
            }

            const blob = (blobCandidate instanceof Blob) ? blobCandidate : new Blob([blobCandidate], { type: resource.mimeType || 'application/octet-stream' });

            const url = window.URL.createObjectURL(blob);
            const a = document.createElement("a");
            a.href = url;
            const safeName = (resource.title && resource.title.replace(/[^a-z0-9.\-_]/gi, "_")) || `resource_${resource.id}`;
            a.download = safeName;
            document.body.appendChild(a);
            a.click();
            a.remove();
            window.URL.revokeObjectURL(url);
        } catch (err) {
            console.error("Download failed:", err);
            window.alert("Failed to download file.");
        } finally {
            setResourceOpLoading(false);
        }
    };

    const handleDeleteResource = async (resourceId) => {
        if (!window.confirm("Delete this resource? This action cannot be undone.")) return;
        try {
            setResourceOpLoading(true);
            await llmResourceAPI.delete(resourceId, true);
            await refreshProfessorGroup();
        } catch (err) {
            console.error("Delete failed:", err);
            window.alert("Failed to delete resource.");
        } finally {
            setResourceOpLoading(false);
        }
    };

    const handleLinkFormChange = (e) => {
        const { name, value } = e.target;
        setLinkForm(prev => ({ ...prev, [name]: value }));
    };

    const handleCreateLink = async () => {
        const { title, url, description } = linkForm;
        if (!url || !title) {
            window.alert("Please provide at least a title and a URL.");
            return;
        }
        try {
            setLinkSaving(true);
            const payload = {
                groupSubjectId: professorGroupId,
                title,
                url,
                description,
            };
            await llmResourceAPI.createLink(payload);
            setLinkForm({ title: "", url: "", description: "" });
            await refreshProfessorGroup();
            window.alert("Link created.");
        } catch (err) {
            console.error("Create link failed:", err);
            window.alert("Failed to create link.");
        } finally {
            setLinkSaving(false);
        }
    };

    const resources = professorGroupSubject?.llmResources || [];
    const isLink = (r) => (String(r?.kind || "").toUpperCase() === "LINK");
    const linkResources = resources.filter(isLink);
    const docResources = resources.filter(r => !isLink(r));

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    const handleToggle = (e) => {
        const { name, checked } = e.target;
        setFormData((prev) => ({ ...prev, [name]: checked }));
    };

    const handleNumberChange = (e) => {
        const { name, value } = e.target;
        if (value === "") {
            setFormData((prev) => ({ ...prev, [name]: "" }));
        } else {
            let num;
            if (name === "topK" || name === "memoryWindowSize") {
                num = parseInt(value, 10);
            } else {
                num = parseFloat(value); // similarityThreshold
            }
            setFormData((prev) => ({ ...prev, [name]: isNaN(num) ? "" : num }));
        }
    };

    const handleParamFieldChange = (id, field, value) => {
        setFormData((prev) => ({
            ...prev,
            params: (prev.params || []).map((p) => (p.id === id ? { ...p, [field]: value } : p)),
        }));
    };

    const handleAddParam = () => {
        setFormData((prev) => ({
            ...prev,
            params: [
                ...(prev.params || []),
                {
                    id: crypto?.randomUUID ? crypto.randomUUID() : `${Date.now()}_${Math.random()}`,
                    k: "",
                    v: "",
                },
            ],
        }));
    };

    const handleRemoveParam = (id) => {
        setFormData((prev) => ({
            ...prev,
            params: (prev.params || []).filter((p) => p.id !== id),
        }));
    };

    const handleSave = async () => {
        setSaving(true);
        setError("");
        try {
            const { topK, similarityThreshold, memoryWindowSize, params, ...rest } = formData;

            const payload = {
                ...(llmControl || {}),
                ...rest,
                params: toParamsObject(params),
                ...(topK === "" ? {} : { topK }),
                ...(similarityThreshold === "" ? {} : { similarityThreshold }),
                ...(memoryWindowSize === "" ? {} : { memoryWindowSize }),
            };

            await llmControlAPI.update(payload);
            setLlmControl(payload);
            window.alert("LLM control updated successfully!");
        } catch (err) {
            console.error("Failed to save LlmControl:", err);
            setError("Couldn't save changes. Please check your inputs and try again.");
            window.alert("Failed to save LLM control.");
        } finally {
            setSaving(false);
        }
    };

    const members = useMemo(() => {
        return professorGroupSubject?.professorMembers || [];
    }, [professorGroupSubject]);

    if (!professorGroupSubject && !error) {
        return (
            <div className="relative flex h-dvh flex-col overflow-hidden">
                <Navbar />
                <div className="grow overflow-auto bg-gray-50 text-gray-900">
                    <div className="mx-auto max-w-5xl p-6">
                        <Header title="Loading…" subtitle="Fetching enrollment details" />
                        <div className="mt-6 animate-pulse space-y-4">
                            <div className="h-24 rounded-2xl bg-gray-200" />
                            <div className="h-56 rounded-2xl bg-gray-200" />
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="relative flex h-dvh flex-col overflow-hidden">
            <Navbar />
            <div className="grow overflow-auto bg-gray-50 text-gray-900">
                <div className="mx-auto max-w-5xl p-6">
                    <Breadcrumbs
                        items={[
                            { label: "Enrollments", to: "/enrollments" },
                            { label: professorGroupSubject?.subjectName || "Enrollment" },
                        ]}
                    />

                    <Header
                        title={professorGroupSubject?.subjectName || "—"}
                        subtitle={professorGroupSubject?.shortName || ""}
                    />

                    {error && (
                        <div className="mt-4 rounded-xl border border-red-200 bg-red-50 p-3 text-sm text-red-700">
                            {error}
                        </div>
                    )}

                    <section className="mt-6 grid gap-6 md:grid-cols-[2fr_1fr] overflow-x-auto">
                        <Card>
                            <CardHeader title="LLM Control" />
                            <div className="mt-4 space-y-4">
                                <Field label="LLM Provider">
                                    <select
                                        name="llmProvider"
                                        value={formData.llmProvider}
                                        onChange={handleInputChange}
                                        className="mt-1 block w-full rounded-xl border border-gray-300 bg-white px-3 py-2 text-gray-900 shadow-sm outline-none ring-1 ring-transparent focus:border-indigo-500 focus:ring-indigo-500"
                                    >
                                        <option value="">Select provider</option>
                                        <option value="openai">OpenAI</option>
                                        <option value="anthropic">Anthropic</option>
                                    </select>
                                </Field>

                                <Field label="Model name">
                                    <input
                                        type="text"
                                        name="modelName"
                                        value={formData.modelName}
                                        onChange={handleInputChange}
                                        className="mt-1 block w-full rounded-xl border border-gray-300 bg-white px-3 py-2 text-gray-900 shadow-sm outline-none ring-1 ring-transparent placeholder:text-gray-400 focus:border-indigo-500 focus:ring-indigo-500"
                                        placeholder="e.g. gpt-4o, claude-3-opus"
                                    />
                                </Field>

                                <Field label="System prompt">
                  <textarea
                      name="systemPrompt"
                      value={formData.systemPrompt}
                      onChange={handleInputChange}
                      rows={5}
                      className="mt-1 block w-full rounded-xl border border-gray-300 bg-white px-3 py-2 text-gray-900 shadow-sm outline-none ring-1 ring-transparent placeholder:text-gray-400 focus:border-indigo-500 focus:ring-indigo-500"
                      placeholder="You are a helpful assistant…"
                  />
                                </Field>

                                <Field label="RAG behavior">
                                    <div className="mt-2 flex flex-wrap items-center gap-6">
                                        <div className="flex items-center gap-3">
                                            <input
                                                id="strictRag"
                                                name="strictRag"
                                                type="checkbox"
                                                checked={!!formData.strictRag}
                                                onChange={handleToggle}
                                                className="h-5 w-5 rounded border-gray-300 text-indigo-600 focus:ring-indigo-500"
                                            />
                                            <label htmlFor="strictRag" className="text-sm text-gray-700">
                                                Strict RAG — use context only
                                            </label>
                                        </div>

                                        <div className="flex items-center gap-3">
                                            <input
                                                id="relaxedAnswers"
                                                name="relaxedAnswers"
                                                type="checkbox"
                                                checked={!!formData.relaxedAnswers}
                                                onChange={handleToggle}
                                                className="h-5 w-5 rounded border-gray-300 text-indigo-600 focus:ring-indigo-500"
                                            />
                                            <label htmlFor="relaxedAnswers" className="text-sm text-gray-700">
                                                Relaxed answers — allow prior knowledge
                                            </label>
                                        </div>
                                    </div>
                                    <p className="mt-2 text-xs text-gray-500">
                                        When both are off, the model answers normally.
                                    </p>
                                </Field>

                                <Field label="Top K">
                                    <input
                                        type="number"
                                        name="topK"
                                        value={formData.topK}
                                        onChange={handleNumberChange}
                                        min={0}
                                        step={1}
                                        className="mt-1 block w-full rounded-xl border border-gray-300 bg-white px-3 py-2 text-gray-900 shadow-sm outline-none ring-1 ring-transparent placeholder:text-gray-400 focus:border-indigo-500 focus:ring-indigo-500"
                                        placeholder="e.g. 5"
                                    />
                                </Field>

                                <Field label="Similarity threshold">
                                    <input
                                        type="number"
                                        name="similarityThreshold"
                                        value={formData.similarityThreshold}
                                        onChange={handleNumberChange}
                                        min={0}
                                        max={1}
                                        step={0.01}
                                        className="mt-1 block w-full rounded-xl border border-gray-300 bg-white px-3 py-2 text-gray-900 shadow-sm outline-none ring-1 ring-transparent placeholder:text-gray-400 focus:border-indigo-500 focus:ring-indigo-500"
                                        placeholder="0.00 – 1.00 (e.g. 0.75)"
                                    />
                                </Field>

                                <Field label="Memory window size">
                                    <input
                                        type="number"
                                        name="memoryWindowSize"
                                        value={formData.memoryWindowSize}
                                        onChange={handleNumberChange}
                                        min={0}
                                        step={1}
                                        className="mt-1 block w-full rounded-xl border border-gray-300 bg-white px-3 py-2 text-gray-900 shadow-sm outline-none ring-1 ring-transparent placeholder:text-gray-400 focus:border-indigo-500 focus:ring-indigo-500"
                                        placeholder="e.g. 20"
                                    />
                                </Field>

                                <Field label="Params">
                                    <div className="space-y-2">
                                        {(formData.params || []).length === 0 && (
                                            <p className="text-sm text-gray-500">No params yet.</p>
                                        )}

                                        {(formData.params || []).map((p) => (
                                            <div key={p.id} className="flex items-center gap-2">
                                                <input
                                                    type="text"
                                                    value={p.k}
                                                    onChange={(e) => handleParamFieldChange(p.id, "k", e.target.value)}
                                                    className="flex-1 rounded-xl border border-gray-300 bg-white px-3 py-2 text-gray-900 outline-none ring-1 ring-transparent placeholder:text-gray-400 focus:border-indigo-500 focus:ring-indigo-500"
                                                    placeholder="Key (e.g. temperature)"
                                                />
                                                <input
                                                    type="text"
                                                    value={p.v}
                                                    onChange={(e) => handleParamFieldChange(p.id, "v", e.target.value)}
                                                    className="flex-1 rounded-xl border border-gray-300 bg-white px-3 py-2 text-gray-900 outline-none ring-1 ring-transparent placeholder:text-gray-400 focus:border-indigo-500 focus:ring-indigo-500"
                                                    placeholder="Value (e.g. 0.2)"
                                                />
                                                <button
                                                    type="button"
                                                    onClick={() => handleRemoveParam(p.id)}
                                                    className="rounded-xl px-3 py-2 text-sm text-red-600 hover:bg-red-50"
                                                    aria-label={`Remove ${p.k || "param"}`}
                                                >
                                                    Remove
                                                </button>
                                            </div>
                                        ))}

                                        <button
                                            type="button"
                                            onClick={handleAddParam}
                                            className="rounded-xl px-3 py-2 text-sm text-indigo-600 hover:bg-indigo-50"
                                        >
                                            + Add param
                                        </button>
                                    </div>
                                </Field>

                                <div className="pt-2">
                                    <button
                                        onClick={handleSave}
                                        disabled={saving}
                                        className="inline-flex items-center justify-center rounded-2xl bg-indigo-600 px-4 py-2 text-white shadow-sm transition hover:bg-indigo-700 disabled:cursor-not-allowed disabled:opacity-60"
                                    >
                                        {saving ? "Saving…" : "Save"}
                                    </button>
                                </div>
                            </div>
                        </Card>

                        <div className="flex flex-col gap-4">
                            <Card>
                                <CardHeader title="Professor Members" />
                                <ul className="mt-3 list-inside list-disc text-sm text-gray-700 max-h-40 overflow-auto">
                                    {members.length === 0 && <li className="text-gray-500">No members</li>}
                                    {members.map((p, idx) => (
                                        <li key={`${p}-${idx}`}>{p}</li>
                                    ))}
                                </ul>
                            </Card>

                            <Card>
                                <CardHeader title="Documents" aside={
                                    <div className="flex items-center gap-2">
                                        <input
                                            ref={fileInputRef}
                                            type="file"
                                            onChange={handleFileSelected}
                                            className="hidden"
                                        />
                                        <button
                                            type="button"
                                            onClick={handleUploadClick}
                                            disabled={fileUploading}
                                            className="rounded-xl px-3 py-1 text-sm text-indigo-600 hover:bg-indigo-50"
                                        >
                                            {fileUploading ? "Uploading…" : "+ Attach file"}
                                        </button>
                                    </div>
                                } />
                                <div className="mt-3 space-y-3">
                                    {docResources.length === 0 && <p className="text-sm text-gray-500">No documents.</p>}
                                    {docResources.map((r) => (
                                        <div key={r.id} className="flex items-center justify-between gap-3">
                                            <div className="min-w-0">
                                                <div className="text-sm font-medium text-gray-900">{r.title || `Document ${r.id}`}</div>
                                                {r.description && <div className="text-xs text-gray-500 truncate">{r.description}</div>}
                                            </div>
                                            <div className="flex items-center gap-2">
                                                <button
                                                    type="button"
                                                    onClick={() => handleDownload(r)}
                                                    disabled={resourceOpLoading}
                                                    className="rounded-xl px-3 py-1 text-sm text-indigo-600 hover:bg-indigo-50"
                                                >
                                                    Download
                                                </button>
                                                <button
                                                    type="button"
                                                    onClick={() => handleDeleteResource(r.id)}
                                                    disabled={resourceOpLoading}
                                                    className="rounded-xl px-3 py-1 text-sm text-red-600 hover:bg-red-50"
                                                >
                                                    Delete
                                                </button>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            </Card>

                            <Card>
                                <CardHeader title="Links" />
                                <div className="mt-3 space-y-3">
                                    <div className="space-y-2">
                                        <input
                                            name="title"
                                            value={linkForm.title}
                                            onChange={handleLinkFormChange}
                                            placeholder="Title"
                                            className="block w-full rounded-xl border border-gray-300 bg-white px-3 py-2 text-gray-900 shadow-sm outline-none placeholder:text-gray-400 focus:border-indigo-500 focus:ring-indigo-500"
                                        />
                                        <input
                                            name="url"
                                            value={linkForm.url}
                                            onChange={handleLinkFormChange}
                                            placeholder="https://..."
                                            className="block w-full rounded-xl border border-gray-300 bg-white px-3 py-2 text-gray-900 shadow-sm outline-none placeholder:text-gray-400 focus:border-indigo-500 focus:ring-indigo-500"
                                        />
                                        <input
                                            name="description"
                                            value={linkForm.description}
                                            onChange={handleLinkFormChange}
                                            placeholder="Description (optional)"
                                            className="block w-full rounded-xl border border-gray-300 bg-white px-3 py-2 text-gray-900 shadow-sm outline-none placeholder:text-gray-400 focus:border-indigo-500 focus:ring-indigo-500"
                                        />
                                        <div className="flex justify-end">
                                            <button
                                                type="button"
                                                onClick={handleCreateLink}
                                                disabled={linkSaving}
                                                className="rounded-2xl bg-indigo-600 px-3 py-1 text-sm text-white hover:bg-indigo-700 disabled:opacity-60"
                                            >
                                                {linkSaving ? "Saving…" : "Save link"}
                                            </button>
                                        </div>
                                    </div>

                                    {linkResources.length === 0 && <p className="text-sm text-gray-500">No links.</p>}
                                    {linkResources.map((r) => (
                                        <div key={r.id} className="flex items-center justify-between gap-3">
                                            <div className="min-w-0">
                                                <a
                                                    href={r.url}
                                                    target="_blank"
                                                    rel="noreferrer"
                                                    className="text-sm font-medium text-indigo-600 hover:underline"
                                                >
                                                    {r.title || r.url}
                                                </a>
                                                {r.description && <div className="text-xs text-gray-500 truncate">{r.description}</div>}
                                            </div>
                                            <div className="flex items-center gap-2">
                                                <a
                                                    href={r.url}
                                                    target="_blank"
                                                    rel="noreferrer"
                                                    className="rounded-xl px-3 py-1 text-sm text-indigo-600 hover:bg-indigo-50"
                                                >
                                                    Open
                                                </a>
                                                <button
                                                    onClick={() => handleDeleteResource(r.id)}
                                                    disabled={resourceOpLoading}
                                                    className="rounded-xl px-3 py-1 text-sm text-red-600 hover:bg-red-50"
                                                >
                                                    Delete
                                                </button>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            </Card>
                        </div>
                    </section>
                </div>
            </div>
        </div>
    );
}
