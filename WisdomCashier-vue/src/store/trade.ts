import { defineStore } from "pinia";
export const useTradeStore = defineStore("Trade", {
  state: () => {
    return {
      tradeList: [],
    };
  },
  getters: {
    get: (state) => state.tradeList,
  },
  actions: {
    //添加
    add(params: any) {
      let boolean = false;
      this.tradeList.forEach((e) => {
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-ignore
        if (e.gid === params.gid) {
          // eslint-disable-next-line @typescript-eslint/ban-ts-comment
          // @ts-ignore
          e.num += 1;
          boolean = true;
        }
      });
      if (boolean === false) {
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-ignore
        this.tradeList.push(params);
      }
    },

    //删除
    del(params: any) {
      this.tradeList = this.tradeList.filter((e) => {
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-ignore
        return e.gid !== params;
      });
    },

    //清空
    clear() {
      this.tradeList = [];
    },

    //指定某条数量
    setOne(gid: any, num: any) {
      this.tradeList.forEach((e) => {
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-ignore
        if (e.gid === gid) {
          // eslint-disable-next-line @typescript-eslint/ban-ts-comment
          // @ts-ignore
          e.num = num;
        }
      });
    },

    set(list: []) {
      this.tradeList = list;
    },
  },
  persist: true,
});
