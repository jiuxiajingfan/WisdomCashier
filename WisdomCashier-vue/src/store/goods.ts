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
    let list = [];
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-ignore
    list = JSON.parse(localStorage.getItem("productList"));
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-ignore
    return {
      productList: list,
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
    save() {
      localStorage.setItem("productList", JSON.stringify(this.productList));
    },
  },
});
