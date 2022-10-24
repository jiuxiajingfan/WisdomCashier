import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import Login from "../views/Login/Login.vue";
import h from "../views/Home/Home.vue";
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
    if (to.path === "/login") {
      next();
    } else {
      const token = localStorage.getItem("token");
      if (token === undefined || token === "") {
        next("/login");
      } else {
        next();
      }
    }
  }
});

export default router;
