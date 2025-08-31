import axios from 'axios';
import { authService } from './auth.js';

const API_BASE_URL = 'http://localhost:8080';

const axiosClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

axiosClient.interceptors.request.use(
  (config) => {
    if (config.includeAuth !== false) {
      const token = authService.getToken();
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

axiosClient.interceptors.response.use(
  (response) => {
    return response.data;
  },
  (error) => {
    if (error.response) {
      const errorMessage = error.response.data?.message || `HTTP error! status: ${error.response.status}`;

      if (error.response.status === 401) {
        authService.removeToken();
        window.location.href = '/login';
      }

      throw new Error(errorMessage);
    } else if (error.request) {
      throw new Error('Network error: No response from server');
    } else {
      throw new Error(error.message || 'An unexpected error occurred');
    }
  }
);

export const httpClient = {
  get: (url, config = {}) => axiosClient.get(url, config),
  post: (url, data = null, config = {}) => axiosClient.post(url, data, config),
  put: (url, data = null, config = {}) => axiosClient.put(url, data, config),
  delete: (url, config = {}) => axiosClient.delete(url, config),
  patch: (url, data = null, config = {}) => axiosClient.patch(url, data, config),
};