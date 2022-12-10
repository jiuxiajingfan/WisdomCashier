import { defineStore } from "pinia";
export const useUserStore = defineStore("user", {
  state: () => {
    const userNames = localStorage.getItem("userName");
    const userIds = localStorage.getItem("userId");
    const userNicknames = localStorage.getItem("userNickname");
    const image = localStorage.getItem("image");
    const phone = localStorage.getItem("phone");
    const email = localStorage.getItem("email");
    return {
      userName: userNames,
      userId: userIds,
      userNickName: userNicknames,
      image: image,
      phone: phone,
      email: email,
    };
  },
  getters: {
    getName: (state) => state.userName,
    getNickName: (state) => state.userNickName,
    getId: (state) => state.userId,
    getImage: (state) => state.image,
    getPhone: (state) => state.phone,
    getEmail: (state) => state.email,
  },
  actions: {
    setName(name: string) {
      localStorage.setItem("userName", name);
      this.userName = name;
    },
    setNickName(name: string) {
      localStorage.setItem("userNickname", name);
      this.userNickName = name;
    },
    setId(id: string) {
      localStorage.setItem("userId", id);
      this.userId = id;
    },
    setImage(path: string) {
      localStorage.setItem("image", path);
      this.image = path;
    },
    setPhone(path: string) {
      localStorage.setItem("phone", path);
      this.phone = path;
    },
    setEmail(path: string) {
      localStorage.setItem("email", path);
      this.email = path;
    },
  },
});
