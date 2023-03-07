import { defineStore } from "pinia";

interface ProductInter {
  name: string;
  num: number;
  priceOut: number;
  picUrl: string;
  metrology: string;
  gid: string;
}

export const useGoodStore = defineStore("Good", {
  state: () => {
    return {
      productList: [],
    };
  },
  getters: {
    get: (state) => state.productList,
  },
  actions: {
    set(list: []) {
      localStorage.setItem("productList", JSON.stringify(list));
      this.productList = list;
    },
    push(param: ProductInter) {
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-ignore
      this.productList.push(param);
    },
  },
});
