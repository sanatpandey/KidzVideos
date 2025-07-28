import axios from 'axios';

const gateway = axios.create({
  baseURL: 'http://localhost:8081/user',
  withCredentials: true, // send cookies (refreshToken)
});

export default gateway;
