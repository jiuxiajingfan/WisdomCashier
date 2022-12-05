import { defineStore } from "pinia";
export const useUserStore = defineStore("user", {
  state: () => {
    const userNames = localStorage.getItem("userName");
    const userIds = localStorage.getItem("userId");
    const userNicknames = localStorage.getItem("userNickname");
    const image = localStorage.getItem("image");
    return {
      userName: userNames,
      userId: userIds,
      userNickName: userNicknames,
      image: image,
    };
  },
  getters: {
    getName: (state) => state.userName,
    getNickName: (state) => state.userNickName,
    getId: (state) => state.userId,
    getImage: (state) => state.image,
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
  },
});
