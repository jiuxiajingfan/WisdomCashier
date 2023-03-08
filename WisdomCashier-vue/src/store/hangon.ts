import { defineStore } from "pinia";

interface IHang {
  id: string;
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
        return item.id !== index;
      });
    },
  },
  persist: true,
});
