import { httpClient } from './axiosClient.js';

// Authentication endpoints
export const authAPI = {
  login: (credentials) =>
    httpClient.post('/api/auth/login', credentials, { includeAuth: false }),
};

// Chat Session endpoints
export const chatSessionAPI = {
  openSession: (subjectId) =>
    httpClient.post(`/api/chat-sessions/subjects/${subjectId}/open`),

  getSession: (sessionId) =>
    httpClient.get(`/api/chat-sessions/${sessionId}`),

  sendMessage: (messageData) =>
    httpClient.post('/api/chat-sessions/messages', messageData),

  deleteSession: (sessionId) =>
    httpClient.delete(`/api/chat-sessions/${sessionId}`),
};

// Enrollment endpoints
export const enrollmentAPI = {
  enrollAndCreateGroup: (subjectId) =>
    httpClient.post(`/api/enrollments/subjects/${subjectId}/create-group`),

  enrollAndJoinGroup: (subjectId, groupId) =>
    httpClient.post(`/api/enrollments/subjects/${subjectId}/join-group/${groupId}`),

  unenroll: (subjectId) =>
    httpClient.delete(`/api/enrollments/subjects/${subjectId}`),
};

// LLM Control endpoints
export const llmControlAPI = {
  getByProfessorGroupSubject: (professorGroupSubjectId) =>
    httpClient.get(`/api/llm-controls/professor-group-subject/${professorGroupSubjectId}`),

  create: (llmControlData) =>
    httpClient.post('/api/llm-controls', llmControlData),

  update: (llmControlData) =>
    httpClient.put('/api/llm-controls', llmControlData),

  delete: (llmControlId) =>
    httpClient.delete(`/api/llm-controls/${llmControlId}`),
};

// Professor Group Subject endpoints
export const professorGroupSubjectAPI = {
  findById: (id) =>
    httpClient.get(`/api/professor-group-subjects/${id}`),

  deleteById: (id) =>
    httpClient.delete(`/api/professor-group-subjects/${id}`),

  changeGroupForSubject: (subjectId, newGroupId) =>
    httpClient.put(`/api/professor-group-subjects/subjects/${subjectId}/change-group/${newGroupId}`),

  getEnrolledSubjects: () =>
    httpClient.get('/api/professor-group-subjects/enrolled-subjects'),
};

export const API = {
  auth: authAPI,
  chatSessions: chatSessionAPI,
  enrollments: enrollmentAPI,
  llmControls: llmControlAPI,
  professorGroupSubjects: professorGroupSubjectAPI,
};

export default API;
