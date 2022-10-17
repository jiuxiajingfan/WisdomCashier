import axios from "axios";

const api = axios.create({
  baseURL: "/api",
  //请求超时时间
  timeout: 30 * 1000,
  //允许跨域
  withCredentials: true,
  // cancelToken: source.token,
  headers: {
    "Content-Type": "application/json;charset=utf-8",
  },
});

//请求拦截器
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.accessToken = token;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

//相应拦截器
api.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    return Promise.reject(error);
  }
);
export default api;
