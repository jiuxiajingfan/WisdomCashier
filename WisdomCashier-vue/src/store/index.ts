import { defineStore } from "pinia";
import { ref } from "vue";
export const useAuthStore = defineStore("auth", {
  state: () => {
    const tokens = sessionStorage.getItem("token");
    return {
      token: tokens,
    };
  },
  getters: {
    getToken: (state) => state.token,
  },
  actions: {
    setToken(token: string) {
      sessionStorage.setItem("token", token);
      this.token = token;
    },
  },
  persist: true,
});
