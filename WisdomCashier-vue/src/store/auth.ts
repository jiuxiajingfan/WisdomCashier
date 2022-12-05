import { defineStore } from "pinia";
export const useAuthStore = defineStore("auth", {
  state: () => {
    const tokens = localStorage.getItem("token");
    return {
      token: tokens,
    };
  },
  getters: {
    getToken: (state) => state.token,
  },
  actions: {
    setToken(token: string) {
      localStorage.setItem("token", token);
      this.token = token;
    },
  },
});
