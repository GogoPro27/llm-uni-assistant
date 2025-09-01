import React, { useState, useEffect } from 'react';
import SubjectSelect from './SubjectSelect.jsx';
import API from '../../../../api/endpoints.js';

function SubjectSelectGrid({ onSubjectSelect, selectedSubject }) {
  const [subjects, setSubjects] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
      API.professorGroupSubjects.getEnrolledSubjects()
          .then(response => {
              console.log('Enrolled subjects:', response);
              setSubjects(response);
              setLoading(false);
          })
          .catch(err => {
              setError('Failed to fetch enrolled subjects');
              console.error('Error fetching enrolled subjects:', err);
              setLoading(false);
          });
  }, []);

  const handleSubjectSelect = (subject) => {
    if (onSubjectSelect) {
      onSubjectSelect(subject);
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center p-8">
        <div className="text-slate-400">Loading enrolled subjects...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex justify-center items-center p-8">
        <div className="text-red-400">{error}</div>
      </div>
    );
  }

  if (subjects.length === 0) {
    return (
      <div className="flex justify-center items-center p-8">
        <div className="text-slate-400">No enrolled subjects found</div>
      </div>
    );
  }

  return (
    <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4 p-4">
      {subjects.map((subject) => (
        <SubjectSelect
          key={subject.id}
          subject={subject}
          isSelected={selectedSubject?.id === subject.id}
          onSelect={handleSubjectSelect}
        />
      ))}
    </div>
  );
}

export default SubjectSelectGrid;