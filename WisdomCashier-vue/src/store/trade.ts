import { defineStore } from "pinia";
import { number } from "mathjs";
import { backtopProps } from "element-plus";
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
        return e.gid !== params.gid;
      });
    },

    //清空
    clear() {
      this.tradeList = [];
    },
  },
  persist: true,
});
