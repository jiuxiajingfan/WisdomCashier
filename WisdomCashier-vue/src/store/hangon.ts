import { defineStore } from "pinia";

interface IHang {
  time: string;
  tip: string;
  list: [];
  sum: number;
}

export const useHangStore = defineStore("Hang", {
  state: () => {
    return {
      hangList: [],
    };
  },
  getters: {
    get: (state) => state.hangList,
  },
  actions: {
    set(list: []) {
      localStorage.setItem("Hang", JSON.stringify(list));
      this.hangList = list;
    },
    add(param: IHang) {
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-ignore
      this.hangList.push(param);
    },
    del(index: any) {
      this.hangList = this.hangList.filter((item) => {
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-ignore
        return item.time !== index;
      });
    },
    getone(index: any) {
      const new1 = this.hangList.filter((item) => {
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-ignore
        return item.time !== index;
      });
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-ignore
      console.log(new1[0].list);
    },
  },
  persist: true,
});
