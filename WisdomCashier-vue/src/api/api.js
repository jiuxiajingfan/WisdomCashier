import axios from "axios";
import utils from "@/utils/utils";
import router from "@/router";

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
    token && (config.headers.Authorization = token);
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

//相应拦截器
axios.interceptors.response.use(
  (response) => {
    if (response.data.code === 200 || response.data.code == undefined) {
      return Promise.resolve(response);
    } else {
      utils.showErrMessage(response.data.msg);
      return Promise.reject(response);
    }
  },
  (error) => {
    if (error.response) {
      if (error.response.data instanceof Blob) {
        // 如果是文件操作的返回，由后续进行处理
        return Promise.resolve(error.response);
      }
      switch (error.response.code) {
        // 401: 未登录 token过期
        // 未登录则跳转登录页面，并携带当前页面的路径
        // 在登录成功后返回当前页面，这一步需要在登录页操作。
        case 401:
          if (error.response.data.msg) {
            utils.showErrMessage(error.response.data.msg);
          }
          router.push("/api/admin/login");
          break;
        // 403
        // 无权限访问或操作的请求
        case 403:
          if (error.response.data.msg) {
            utils.showErrMessage(error.response.data.msg);
          }
          break;
        // 404请求不存在
        case 404:
          utils.showErrMessage("404");
          break;
        // 其他错误，直接抛出错误提示
        default:
          if (error.response.data) {
            if (error.response.data.msg) {
              utils.showErrMessage(error.response.data.msg);
            } else {
              utils.showErrMessage("报错了！");
            }
          }
          break;
      }
      return Promise.reject(error);
    } else {
      //处理断网或请求超时，请求没响应
      if (error.code == "ECONNABORTED" || error.message.includes("timeout")) {
        utils.showErrMessage("检查网络");
      } else {
        utils.showErrMessage("为响应");
      }
      return Promise.reject(error);
    }
  }
);

export default api;
