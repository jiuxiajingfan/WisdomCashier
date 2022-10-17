import { ElMessage } from "element-plus";
const utils = {
  saveData(key, value) {
    localStorage.setItem(key, value);
  },
  remove(key) {
    localStorage.removeItem(key);
  },
  getData(key) {
    return localStorage.getItem(key);
  },
  showMessage(code, msg) {
    if (code == 0) {
      ElMessage({
        showClose: true,
        message: msg,
        type: "success",
      });
    } else {
      ElMessage({
        showClose: true,
        message: msg,
        type: "error",
      });
    }
  },
};
export default utils;
