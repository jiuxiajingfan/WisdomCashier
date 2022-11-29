import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import Login from "../views/Login/Login.vue";
import h from "../views/Home/Home.vue";
import pinia from "@/store/store";
import { useAuthStore } from "../store/index";
import { ElMessage } from "element-plus";
const store = useAuthStore(pinia);
const routes: Array<RouteRecordRaw> = [
  {
    path: "/login",
    name: "login",
    component: Login,
  },
  {
    path: "/home",
    name: "home",
    component: h,
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
      if (token != "") {
        console.log("有token登录");
        next("/home");
      } else {
        next();
      }
    } else {
      if (token === undefined || token === "") {
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
