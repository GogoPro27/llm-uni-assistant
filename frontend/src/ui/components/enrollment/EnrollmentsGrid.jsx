// EnrollmentsGrid.jsx
import React, {useCallback, useEffect, useState} from 'react';
import {API, enrollmentAPI} from '../../../api/endpoints.js';
import EnrollmentCard from './EnrollmentCard.jsx';
import SubjectEnrollDialog from './SubjectEnrollDialog.jsx';

function EnrollmentsGrid() {
    const userRoles = JSON.parse(localStorage.getItem('userRoles')) || [];
    const isProfessor = userRoles.includes('professor');

    const [enrollments, setEnrollments] = useState([]);
    const [loading, setLoading] = useState(true);

    // Dialog state
    const [showDialog, setShowDialog] = useState(false);
    const [subjects, setSubjects] = useState([]);
    const [subjectsLoading, setSubjectsLoading] = useState(false);
    const [subjectsError, setSubjectsError] = useState(null);
    const [selectedSubjectId, setSelectedSubjectId] = useState(null);
    const [groupChoice, setGroupChoice] = useState(null); // 'create' | 'join'

    // Group selection state (for join)
    const [groups, setGroups] = useState([]);
    const [groupsLoading, setGroupsLoading] = useState(false);
    const [groupsError, setGroupsError] = useState(null);
    const [selectedGroupId, setSelectedGroupId] = useState(null);

    // Submit state
    const [submitting, setSubmitting] = useState(false);
    const [submitError, setSubmitError] = useState(null);

    const fetchEnrollments = useCallback(async () => {
        try {
            const response = await enrollmentAPI.getEnrolledSubjects();
            setEnrollments(response);
        } catch (error) {
            console.error('Failed to fetch enrollments:', error);
        }
    }, []);

    useEffect(() => {
        (async () => {
            await fetchEnrollments();
            setLoading(false);
        })();
    }, [fetchEnrollments]);

    const openDialog = async () => {
        setShowDialog(true);

        // reset dialog state
        setSubjects([]);
        setSelectedSubjectId(null);
        setGroupChoice(null);
        setSubjectsError(null);
        setSubjectsLoading(true);

        // reset groups state
        setGroups([]);
        setGroupsLoading(false);
        setGroupsError(null);
        setSelectedGroupId(null);

        // reset submit state
        setSubmitting(false);
        setSubmitError(null);

        try {
            const res = await enrollmentAPI.getSubjectsNotEnrolled();
            setSubjects(Array.isArray(res) ? res : []);
        } catch (err) {
            console.error('Failed to fetch subjects not enrolled:', err);
            setSubjectsError('Could not load subjects. Please try again.');
        } finally {
            setSubjectsLoading(false);
        }
    };

    const closeDialog = () => setShowDialog(false);

    // Fetch groups for JOIN when subject selected
    useEffect(() => {
        const fetchGroups = async () => {
            if (groupChoice !== 'join' || !selectedSubjectId) {
                setGroups([]);
                setSelectedGroupId(null);
                setGroupsError(null);
                setGroupsLoading(false);
                return;
            }
            setGroups([]);
            setSelectedGroupId(null);
            setGroupsError(null);
            setGroupsLoading(true);
            try {
                const res = await API.professorGroupSubjects.getAllGroupsBySubjectId(selectedSubjectId);
                setGroups(Array.isArray(res) ? res : []);
            } catch (err) {
                console.error('Failed to fetch professor groups:', err);
                setGroupsError('Could not load groups for this subject.');
            } finally {
                setGroupsLoading(false);
            }
        };
        fetchGroups();
    }, [groupChoice, selectedSubjectId]);

    const canContinue =
        selectedSubjectId != null &&
        (groupChoice === 'create' || (groupChoice === 'join' && selectedGroupId != null));

    const handleContinue = async () => {
        if (!canContinue || submitting) return;
        setSubmitting(true);
        setSubmitError(null);
        try {
            if (groupChoice === 'create') {
                // API.enrollments.enrollAndCreateGroup(subjectId)
                await API.enrollments.enrollAndCreateGroup(selectedSubjectId);
            } else {
                // API.enrollments.enrollAndJoinGroup(subjectId, groupId)
                await API.enrollments.enrollAndJoinGroup(selectedSubjectId, selectedGroupId);
            }

            // Refresh grid so the new card appears
            await fetchEnrollments();

            // Close dialog
            closeDialog();
        } catch (err) {
            console.error('Enrollment action failed:', err);
            setSubmitError(
                err?.message || 'Something went wrong while processing your enrollment.'
            );
        } finally {
            setSubmitting(false);
        }
    };

    if (loading) {
        return <p className="text-center text-gray-500">Loading enrollments...</p>;
    }

    return (
        <div className="p-4">
            {isProfessor && (
                <div className="mb-4 flex justify-end">
                    <button
                        onClick={openDialog}
                        className="inline-flex items-center gap-2 rounded-2xl bg-indigo-600 px-4 py-2 text-white shadow hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                    >
                        + Enroll to a subject
                    </button>
                </div>
            )}

            {enrollments.length === 0 ? (
                <p className="text-center text-gray-500">No enrollments found.</p>
            ) : (
                <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4">
                    {enrollments.map((enrollment) => (
                        <EnrollmentCard key={enrollment.subjectId} enrollment={enrollment}/>
                    ))}
                </div>
            )}

            {showDialog && (
                <SubjectEnrollDialog
                    onClose={closeDialog}
                    subjects={subjects}
                    subjectsLoading={subjectsLoading}
                    subjectsError={subjectsError}
                    selectedSubjectId={selectedSubjectId}
                    setSelectedSubjectId={(id) => {
                        setSelectedSubjectId(id);
                        // clear previous errors/selections when subject changes
                        setSelectedGroupId(null);
                        setSubmitError(null);
                    }}
                    groupChoice={groupChoice}
                    setGroupChoice={(choice) => {
                        setGroupChoice(choice);
                        setSubmitError(null);
                    }}
                    // groups props
                    groups={groups}
                    groupsLoading={groupsLoading}
                    groupsError={groupsError}
                    selectedGroupId={selectedGroupId}
                    setSelectedGroupId={(id) => {
                        setSelectedGroupId(id);
                        setSubmitError(null);
                    }}
                    canContinue={canContinue}
                    onContinue={handleContinue}
                    submitting={submitting}          // NEW
                    submitError={submitError}        // NEW
                />
            )}
        </div>
    );
}

export default EnrollmentsGrid;