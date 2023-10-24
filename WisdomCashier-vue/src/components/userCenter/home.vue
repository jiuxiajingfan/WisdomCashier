<template>
  <p class="ff">欢迎使用,{{ userNickName }}</p>
  <div>
    <el-carousel indicator-position="outside">
      <el-carousel-item v-for="item in img" :key="item.id">
        <img :src="item.url" />
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script setup>
import { storeToRefs } from "pinia/dist/pinia";
import { useUserStore } from "@/store/user";
import pinia from "@/store/store";
const user = useUserStore(pinia);
const { userNickName } = storeToRefs(user);
const img = [
  {
    id: 0,
    url: "https://raw.githubusercontent.com/miccall/MyTextures/master/248913-106.jpg",
  },
  {
    id: 1,
    url: "https://raw.githubusercontent.com/miccall/MyTextures/master/248906-106.jpg",
  },
];
</script>

<style scoped>
.ff {
  font-family: "Google Sans", Roboto, Arial, sans-serif;
  line-height: 2.25rem;
  font-size: 1.75rem;
  letter-spacing: 0;
  font-weight: 400;
  -webkit-hyphens: auto;
  hyphens: auto;
  word-break: break-word;
  word-wrap: break-word;
  color: var(--gm3-sys-color-on-background, #1f1f1f);
  text-align: center;
}
</style>
