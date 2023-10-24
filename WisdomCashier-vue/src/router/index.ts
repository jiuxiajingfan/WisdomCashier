import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import loginPage from "../views/Login/LoginPage.vue";
import choiceShop from "../views/Home/ChoiceShop.vue";
import userCenter from "../views/Home/UserCenter.vue";
import myMessage from "../components/userCenter/myMessage.vue";
import CreateShop from "../components/Shop/CreateShop.vue";
import home from "../components/userCenter/home.vue";
import myShop from "../components/userCenter/myShop.vue";
import err404 from "../views/Error/404.vue";
import shop from "../views/Shop/Shop.vue";
import pinia from "@/store/store";
import { useAuthStore } from "../store/auth";
import { ElMessage } from "element-plus";

const store = useAuthStore(pinia);
const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    redirect: "/login",
  },
  {
    path: "/login",
    name: "login",
    component: loginPage,
  },
  {
    path: "/userCenter",
    name: "userCenter",
    component: userCenter,
    meta: {
      auth: true,
    },
    children: [
      {
        path: "/home",
        name: "home",
        component: home,
        meta: {
          auth: true,
        },
      },
      {
        path: "/myMessage",
        name: "myMessage",
        component: myMessage,
        meta: {
          auth: true,
        },
      },
      {
        path: "/myShop",
        name: "myShop",
        component: myShop,
        meta: {
          auth: true,
        },
      },
      {
        path: "/CreateShop",
        name: "CreateShop",
        component: CreateShop,
        meta: {
          auth: true,
        },
      },
    ],
  },
  {
    path: "/shop",
    name: "shop",
    component: shop,
    meta: {
      auth: true,
    },
  },
  {
    path: "/:pathMatch(.*)*",
    name: "404",
    component: err404,
    meta: {
      auth: false,
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
        next("/home");
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
