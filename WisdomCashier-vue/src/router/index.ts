import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import loginPage from "../views/Login/LoginPage.vue";
import choiceShop from "../views/Home/ChoiceShop.vue";
import userCenter from "../views/Home/UserCenter.vue";
import myMessage from "../components/userCenter/myMessage.vue";
import changeMessage from "../components/userCenter/changeMessage.vue";
import CreateShop from "../components/Shop/CreateShop.vue";
import home from "../components/userCenter/home.vue";
import myShop from "../components/userCenter/myShop.vue";
import charge from "../components/Goods/Charge.vue";
import tradeRecode from "../components/Goods/TradeRecode.vue";
import goodsList from "../components/Goods/GoodsList.vue";
import tadeDigital from "../components/Shop/TradeDigital.vue";
import temporary from "../components/Goods/Temporary.vue";
import shopMessage from "../components/Shop/ShopMessage.vue";
import personManage from "../components/Shop/PersonManage.vue";
import vipManage from "../components/VipManage/VipManage.vue";
import messagePush from "../components/VipManage/MessagePush.vue";
import volume from "../components/Goods/Volume.vue";
import classificationManage from "../components/Goods/ClassificationManage.vue";

import refund from "../components/Goods/Refund.vue";
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
        path: "/createShop",
        name: "createShop",
        component: CreateShop,
        meta: {
          auth: true,
        },
      },
      {
        path: "/changeMessage",
        name: "changeMessage",
        component: changeMessage,
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
    children: [
      {
        path: "/goodsList",
        name: "goodsList",
        component: goodsList,
        meta: {
          auth: true,
        },
      },
      {
        path: "/charge",
        name: "charge",
        component: charge,
        meta: {
          auth: true,
        },
      },
      {
        path: "/refund",
        name: "refund",
        component: refund,
        meta: {
          auth: true,
        },
      },
      {
        path: "/tradeRecode",
        name: "tradeRecode",
        component: tradeRecode,
        meta: {
          auth: true,
        },
      },
      {
        path: "/goodsList",
        name: "goodsList",
        component: goodsList,
        meta: {
          auth: true,
        },
      },
      {
        path: "/temporary",
        name: "temporary",
        component: temporary,
        meta: {
          auth: true,
        },
      },
      {
        path: "/classificationManage",
        name: "classificationManage",
        component: classificationManage,
        meta: {
          auth: true,
        },
      },
      {
        path: "/tadeDigital",
        name: "tadeDigital",
        component: tadeDigital,
        meta: {
          auth: true,
        },
      },
      {
        path: "/shopMessage",
        name: "shopMessage",
        component: shopMessage,
        meta: {
          auth: true,
        },
      },
      {
        path: "/personManage",
        name: "personManage",
        component: personManage,
        meta: {
          auth: true,
        },
      },
      {
        path: "/vipManage",
        name: "vipManage",
        component: vipManage,
        meta: {
          auth: true,
        },
      },
      {
        path: "/messagePush",
        name: "messagePush",
        component: messagePush,
        meta: {
          auth: true,
        },
      },
      {
        path: "/volume",
        name: "volume",
        component: volume,
        meta: {
          auth: true,
        },
      },
    ],
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
