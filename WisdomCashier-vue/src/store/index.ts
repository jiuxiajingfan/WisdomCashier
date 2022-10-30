import { defineStore } from "pinia";

export const useAuthStore = defineStore("auth", {
  state: () => {
    return { token: "" };
  },
  getters: {
    getToken: (state) => state.token,
  },
  actions: {
    setToken(token: string) {
      this.token = token;
    },
  },
});
