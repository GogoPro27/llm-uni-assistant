import React, {useEffect, useMemo, useState} from "react";
import {Link, useParams} from "react-router-dom";
import {llmControlAPI, professorGroupSubjectAPI} from "../../../api/endpoints.js";
import Navbar from "../common/Navbar.jsx";

export default function EnrollmentDetailsPage() {
    const {professorGroupId} = useParams();

    const [professorGroupSubject, setProfessorGroupSubject] = useState(null);
    const [llmControl, setLlmControl] = useState(null);
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState("");

    const [formData, setFormData] = useState({
        llmProvider: "",
        modelName: "",
        systemPrompt: "",
        params: {},
    });

    useEffect(() => {
        let active = true;
        (async () => {
            try {
                setError("");
                const response = await professorGroupSubjectAPI.findById(professorGroupId);
                console.log("Fetched details:", response);
                if (!active) return;
                setProfessorGroupSubject(response);
                if (response?.llmControl) {
                    setLlmControl(response.llmControl);
                    setFormData({
                        llmProvider: response.llmControl.llmProvider || "",
                        modelName: response.llmControl.modelName || "",
                        systemPrompt: response.llmControl.systemPrompt || "",
                        params: response.llmControl.params || {},
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

    const handleInputChange = (e) => {
        const {name, value} = e.target;
        setFormData((prev) => ({...prev, [name]: value}));
    };

    const handleParamChange = (oldKey, newKey, newValue) => {
        setFormData((prev) => {
            const updated = {...(prev.params || {})};
            if (oldKey !== newKey) delete updated[oldKey];
            updated[newKey] = newValue;
            return {...prev, params: updated};
        });
    };

    const handleAddParam = () => {
        setFormData((prev) => {
            const next = {...(prev.params || {})};
            let draftKey = "";
            let i = 1;
            while (Object.prototype.hasOwnProperty.call(next, draftKey)) {
                draftKey = `key_${i++}`;
            }
            next[draftKey] = "";
            return {...prev, params: next};
        });
    };

    const handleRemoveParam = (key) => {
        setFormData((prev) => {
            const updated = {...(prev.params || {})};
            delete updated[key];
            return {...prev, params: updated};
        });
    };

    const handleSave = async () => {
        setSaving(true);
        setError("");
        try {
            const payload = {...(llmControl || {}), ...formData};
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
            <div className="flex flex-col h-full">
                <Navbar/>
                <div className="min-h-screen bg-gray-50 text-gray-900">
                    <div className="mx-auto max-w-5xl p-6">
                        <Header title="Loading…" subtitle="Fetching enrollment details"/>
                        <div className="mt-6 animate-pulse space-y-4">
                            <div className="h-24 rounded-2xl bg-gray-200"/>
                            <div className="h-56 rounded-2xl bg-gray-200"/>
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="flex flex-col h-full">
            <Navbar/>
            <div className="min-h-screen bg-gray-50 text-gray-900">
                <div className="mx-auto max-w-5xl p-6">
                    <Breadcrumbs
                        items={[
                            {label: "Enrollments", to: "/enrollments"},
                            {label: professorGroupSubject?.subjectName || "Enrollment"},
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

                    {/* Layout: main on left, small box on right */}
                    <section className="mt-6 grid gap-6 md:grid-cols-[2fr_1fr]">
                        {/* LLM Control on the left */}
                        <Card>
                            <CardHeader title="LLM Control"/>
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

                                <Field label="Params">
                                    <div className="space-y-2">
                                        {Object.entries(formData.params || {}).map(([key, value], i) => (
                                            <div key={`${key}-${i}`} className="flex items-center gap-2">
                                                <input
                                                    type="text"
                                                    value={key}
                                                    onChange={(e) => handleParamChange(key, e.target.value, value)}
                                                    className="flex-1 rounded-xl border border-gray-300 bg-white px-3 py-2 text-gray-900 outline-none ring-1 ring-transparent placeholder:text-gray-400 focus:border-indigo-500 focus:ring-indigo-500"
                                                    placeholder="Key (e.g. temperature)"
                                                />
                                                <input
                                                    type="text"
                                                    value={String(value)}
                                                    onChange={(e) => handleParamChange(key, key, e.target.value)}
                                                    className="flex-1 rounded-xl border border-gray-300 bg-white px-3 py-2 text-gray-900 outline-none ring-1 ring-transparent placeholder:text-gray-400 focus:border-indigo-500 focus:ring-indigo-500"
                                                    placeholder="Value (e.g. 0.2)"
                                                />
                                                <button
                                                    type="button"
                                                    onClick={() => handleRemoveParam(key)}
                                                    className="rounded-xl px-3 py-2 text-sm text-red-600 hover:bg-red-50"
                                                    aria-label={`Remove ${key}`}
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

                        {/* Professors on the right */}
                        <Card>
                            <CardHeader title="Professor Members"/>
                            <ul className="mt-3 list-inside list-disc text-sm text-gray-700">
                                {members.length === 0 && <li className="text-gray-500">No members</li>}
                                {members.map((p, idx) => (
                                    <li key={`${p}-${idx}`}>{p}</li>
                                ))}
                            </ul>
                        </Card>
                    </section>
                </div>
            </div>
        </div>
    );
}

function Header({title, subtitle}) {
    return (
        <div className="mt-2">
            <h1 className="text-3xl font-semibold tracking-tight text-gray-900">{title}</h1>
            {subtitle ? (
                <p className="mt-1 text-base text-gray-600">{subtitle}</p>
            ) : null}
        </div>
    );
}

function Breadcrumbs({items}) {
    if (!items?.length) return null;
    return (
        <nav className="text-sm text-gray-600" aria-label="Breadcrumb">
            <ol className="flex items-center gap-2">
                {items.map((item, idx) => (
                    <li key={`${item.label}-${idx}`} className="flex items-center gap-2">
                        {item.to ? (
                            <Link
                                to={item.to}
                                className="text-indigo-600 hover:text-indigo-700 hover:underline"
                            >
                                {item.label}
                            </Link>
                        ) : (
                            <span className="text-gray-700">{item.label}</span>
                        )}
                        {idx < items.length - 1 && <span className="text-gray-400">/</span>}
                    </li>
                ))}
            </ol>
        </nav>
    );
}

function Card({children}) {
    return (
        <div className="rounded-2xl border border-gray-200 bg-white p-5 shadow-sm">
            {children}
        </div>
    );
}

function CardHeader({title, aside}) {
    return (
        <div className="flex items-center justify-between">
            <h3 className="text-base font-semibold text-gray-900">{title}</h3>
            {aside}
        </div>
    );
}

function Field({label, children}) {
    return (
        <label className="block">
            <span className="block text-sm font-medium text-gray-700">{label}</span>
            {children}
        </label>
    );
}