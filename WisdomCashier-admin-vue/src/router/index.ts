import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import LoginPage from "../views/login/LoginPage.vue";
import userCenter from "../components/userCenter/userCenter.vue";
import { useAuthStore } from "@/store/auth";
import pinia from "@/store/store";
import { ElMessage } from "element-plus";

const Auth = useAuthStore(pinia);
const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    redirect: "/login",
  },
  {
    path: "/login",
    name: "login",
    component: LoginPage,
  },
  {
    path: "/userCenter",
    name: "userCenter",
    component: userCenter,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

router.beforeEach((to, from, next) => {
  if (!!to.meta && to.meta.auth === false) {
    next();
    return;
  } else {
    const token = Auth.getToken;
    console.log(token);
    if (to.path === "/login") {
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-ignore
      if (token && "null" !== token) {
        next("/userCenter");
      } else {
        next();
      }
    } else {
      if (token === null || "" === token || token === "null") {
        next("/login");
        ElMessage({
          showClose: true,
          message: "请先登录！",
          type: "error",
        });
      } else {
        next();
      }
    }
  }
});

export default router;
