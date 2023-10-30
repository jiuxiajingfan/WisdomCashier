<template>
  <p class="ff">欢迎使用,{{ userNickName }}</p>
  <div>
    <el-carousel indicator-position="outside" height="40vh">
      <el-carousel-item v-for="(item, index) in img" :key="index">
        <img :src="item" />
      </el-carousel-item>
    </el-carousel>
  </div>
  <div>
    <el-row style="margin-top: 20px" :gutter="5">
      <el-col :span="8">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>行业数据</span>
            </div>
          </template>
          <div v-for="o in 4" :key="o" class="text item">
            {{ "List item " + o }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>新闻中心</span>
            </div>
          </template>
          <el-table :data="newSData" max-height="25vh" show-header="false">
            <el-table-column align="center" property="title"></el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>活动公告</span>
            </div>
          </template>
          <el-table :data="activityData" max-height="25vh" show-header="false">
            <el-table-column align="center" property="title"></el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { storeToRefs } from "pinia/dist/pinia";
import { useUserStore } from "@/store/user";
import pinia from "@/store/store";
import router from "@/router";
import api from "@/api/api";
import { onBeforeMount, ref } from "vue";
const user = useUserStore(pinia);
const { userNickName } = storeToRefs(user);
const img = ref([]);
const newSData = ref([]);
const activityData = ref([]);
onBeforeMount(() => {
  api.get("news/getNews").then((res) => {
    newSData.value = res.data.data.newsList;
    activityData.value = res.data.data.activeList;
  });
  api.get("news/getAdvertising").then((res) => {
    img.value = res.data.data;
    console.log(img.value);
  });
});
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
.box-card {
  height: 35vh;
}
</style>
