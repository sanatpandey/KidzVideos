// src/api/axiosSecured.jsx
import axios from 'axios';

const axiosSecured = axios.create({
  baseURL: 'http://localhost:8082', // direct service access
  withCredentials: true,
});

axiosSecured.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken');
    console.log(`Intercepter log token: ${token}`);
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

axiosSecured.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    if (error.response && error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      try {
        const refreshRes = await axios.post(
          'http://localhost:8080/api/auth/refresh-token',
          {},
          { withCredentials: true }
        );

        const newAccessToken = refreshRes.data.accessToken;
        localStorage.setItem('authToken', newAccessToken);

        originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;
        return axiosSecured(originalRequest);
      } catch (err) {
        console.error("Token refresh failed:", err);
        localStorage.removeItem('authToken');
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export default axiosSecured;
