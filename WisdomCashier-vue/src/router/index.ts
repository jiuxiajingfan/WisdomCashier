import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import loginPage from "../views/Login/LoginPage.vue";
import choiceShop from "../views/Home/choiceShop.vue";
import userCenter from "../views/Home/UserCenter.vue";
import pinia from "@/store/store";
import { useAuthStore } from "../store/auth";
import { ElMessage } from "element-plus";
const store = useAuthStore(pinia);
const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "login",
    component: loginPage,
  },
  {
    path: "/login",
    name: "login",
    component: loginPage,
  },
  {
    path: "/choiceShop",
    name: "choiceShop",
    component: choiceShop,
    meta: {
      auth: true,
    },
  },
  {
    path: "/userCenter",
    name: "userCenter",
    component: userCenter,
    meta: {
      auth: true,
    },
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
    const token = store.getToken;
    if (to.path === "/login") {
      if (token && token != "null") {
        console.log("有token登录" + token);
        next("/choiceShop");
      } else {
        next();
      }
    } else {
      if (token === null || token === "" || token === "null") {
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
