// SubjectEnrollDialog.jsx
import React from 'react';

function SubjectEnrollDialog({
                                 onClose,
                                 subjects,
                                 subjectsLoading,
                                 subjectsError,
                                 selectedSubjectId,
                                 setSelectedSubjectId,
                                 groupChoice,
                                 setGroupChoice,
                                 groups,
                                 groupsLoading,
                                 groupsError,
                                 selectedGroupId,
                                 setSelectedGroupId,
                                 canContinue,
                                 onContinue,
                                 submitting = false,      // NEW
                                 submitError = null,      // NEW
                             }) {
    return (
        <div
            className="fixed inset-0 z-50 flex items-center justify-center p-4"
            role="dialog"
            aria-modal="true"
        >
            {/* Backdrop */}
            <div
                className="absolute inset-0 bg-black/40"
                onClick={submitting ? undefined : onClose}  // prevent close while submitting
                aria-hidden="true"
            />

            {/* Panel */}
            <div className="relative z-10 w-full max-w-xl rounded-2xl bg-white p-6 shadow-lg">
                <div className="mb-4 flex items-start justify-between">
                    <h2 className="text-xl font-semibold">Enroll to a subject</h2>
                    <button
                        onClick={onClose}
                        className="rounded-lg p-2 text-gray-500 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-indigo-500 disabled:opacity-50"
                        aria-label="Close dialog"
                        title="Close"
                        disabled={submitting}
                    >
                        ✕
                    </button>
                </div>

                {/* Subjects block */}
                <div className="mb-6">
                    <p className="mb-2 text-sm font-medium text-gray-700">Choose a subject:</p>

                    {subjectsLoading && (
                        <div className="rounded-xl border border-gray-200 p-3 text-gray-500">
                            Loading available subjects…
                        </div>
                    )}

                    {subjectsError && (
                        <div className="rounded-xl border border-red-200 bg-red-50 p-3 text-red-700">
                            {subjectsError}
                        </div>
                    )}

                    {!subjectsLoading && !subjectsError && subjects.length === 0 && (
                        <div className="rounded-xl border border-gray-200 bg-gray-50 p-3 text-gray-600">
                            You’re already enrolled in all available subjects.
                        </div>
                    )}

                    {!subjectsLoading && !subjectsError && subjects.length > 0 && (
                        <ul className="max-h-60 space-y-2 overflow-auto rounded-xl border border-gray-200 p-2">
                            {subjects.map((s) => (
                                <li key={s.id}>
                                    <label
                                        className="flex cursor-pointer items-center gap-3 rounded-xl p-2 hover:bg-gray-50">
                                        <input
                                            type="radio"
                                            name="subject"
                                            value={s.id}
                                            checked={selectedSubjectId === s.id}
                                            onChange={() => setSelectedSubjectId(s.id)}
                                            className="h-4 w-4"
                                            disabled={submitting}
                                        />
                                        <span className="text-gray-800">{s.shortName}</span>
                                    </label>
                                </li>
                            ))}
                        </ul>
                    )}
                </div>

                {/* Group choice */}
                <div className="mb-6">
                    <p className="mb-2 text-sm font-medium text-gray-700">How do you want to participate?</p>
                    <div className="grid gap-2 sm:grid-cols-2">
                        <label
                            className="flex cursor-pointer items-center gap-3 rounded-xl border border-gray-200 p-3 hover:bg-gray-50">
                            <input
                                type="radio"
                                name="groupChoice"
                                value="create"
                                checked={groupChoice === 'create'}
                                onChange={() => setGroupChoice('create')}
                                className="h-4 w-4"
                                disabled={submitting}
                            />
                            <div>
                                <div className="font-medium text-gray-900">Create a professor group</div>
                                <div className="text-sm text-gray-600">Start a new group for this subject.</div>
                            </div>
                        </label>

                        <label
                            className="flex cursor-pointer items-center gap-3 rounded-xl border border-gray-200 p-3 hover:bg-gray-50">
                            <input
                                type="radio"
                                name="groupChoice"
                                value="join"
                                checked={groupChoice === 'join'}
                                onChange={() => setGroupChoice('join')}
                                className="h-4 w-4"
                                disabled={submitting}
                            />
                            <div>
                                <div className="font-medium text-gray-900">Join an existing group</div>
                                <div className="text-sm text-gray-600">Pick a group if available.</div>
                            </div>
                        </label>
                    </div>
                </div>

                {/* Group picker (join only) */}
                {groupChoice === 'join' && (
                    <div className="mb-6">
                        <p className="mb-2 text-sm font-medium text-gray-700">Choose a professor group:</p>

                        {!selectedSubjectId && (
                            <div className="rounded-xl border border-amber-200 bg-amber-50 p-3 text-amber-800">
                                Please select a subject first to see its groups.
                            </div>
                        )}

                        {selectedSubjectId && groupsLoading && (
                            <div className="rounded-xl border border-gray-200 p-3 text-gray-500">Loading groups…</div>
                        )}

                        {selectedSubjectId && groupsError && (
                            <div
                                className="rounded-xl border border-red-200 bg-red-50 p-3 text-red-700">{groupsError}</div>
                        )}

                        {selectedSubjectId && !groupsLoading && !groupsError && groups.length === 0 && (
                            <div className="rounded-xl border border-gray-200 bg-gray-50 p-3 text-gray-600">
                                No professor groups found for this subject.
                            </div>
                        )}

                        {selectedSubjectId && !groupsLoading && !groupsError && groups.length > 0 && (
                            <ul className="max-h-60 space-y-2 overflow-auto rounded-xl border border-gray-200 p-2">
                                {groups.map((g) => (
                                    <li key={g.id}>
                                        <label
                                            className="flex cursor-pointer items-center gap-3 rounded-xl p-2 hover:bg-gray-50">
                                            <input
                                                type="radio"
                                                name="group"
                                                value={g.id}
                                                checked={selectedGroupId === g.id}
                                                onChange={() => setSelectedGroupId(g.id)}
                                                className="h-4 w-4"
                                                disabled={submitting}
                                            />
                                            <span className="text-gray-800">{g.shortName}</span>
                                        </label>
                                    </li>
                                ))}
                            </ul>
                        )}
                    </div>
                )}

                {/* Submit error (if any) */}
                {submitError && (
                    <div className="mb-4 rounded-xl border border-red-200 bg-red-50 p-3 text-red-700">
                        {submitError}
                    </div>
                )}

                {/* Actions */}
                <div className="flex items-center justify-end gap-3">
                    <button
                        onClick={onClose}
                        className="rounded-2xl border border-gray-300 px-4 py-2 text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-gray-300 disabled:opacity-50"
                        disabled={submitting}
                    >
                        Cancel
                    </button>
                    <button
                        onClick={onContinue}
                        disabled={!canContinue || submitting}
                        className={`rounded-2xl px-4 py-2 text-white shadow focus:outline-none focus:ring-2 focus:ring-indigo-500 ${
                            !canContinue || submitting
                                ? 'bg-indigo-300 cursor-not-allowed'
                                : 'bg-indigo-600 hover:bg-indigo-700'
                        }`}
                    >
                        {submitting ? 'Processing…' : 'Continue'}
                    </button>
                </div>
            </div>
        </div>
    );
}

export default SubjectEnrollDialog;