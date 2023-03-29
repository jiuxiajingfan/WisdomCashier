import { defineStore } from "pinia";

interface IUser {
  userName: string;
  userId: string;
  userNickName: string;
  image: string;
  phone: string;
  email: string;
}
export const useUserStore = defineStore("User", {
  state: () => {
    return {
      user: [],
    };
  },
  getters: {
    get: (state) => state.user,
  },
  actions: {
    add(param: IUser) {
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-ignore
      this.user.push(param);
    },
    clear() {
      this.user = [];
    },
  },
  persist: true,
});
