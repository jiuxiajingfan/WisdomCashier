import { createStore } from "vuex";

const store = createStore({
  state() {
    return {
      //当前登录的用户信息
      userinfo: {},
      //监测当前用户是否登录
      islogin: false,
    };
  },
  mutations: {},
});

export default store;
