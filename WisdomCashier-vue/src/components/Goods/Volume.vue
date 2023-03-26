<template>
  <div class="table">
    <h2>本月商品销量排行</h2>
    <el-table
      :data="good"
      height="calc(100vh - 230px)"
      empty-text="暂无销售订单"
    >
      <el-table-column prop="gid" label="商品条码" width="auto" />
      <el-table-column prop="name" label="商品名" width="auto" />
      <el-table-column prop="metrology" label="单位" width="auto" />
      <el-table-column prop="num" label="售出数量" width="auto" />
    </el-table>
  </div>
</template>

<script setup>
import { onBeforeMount, ref } from "vue";
import api from "@/api/api";
import router from "@/router";

let good = ref([]);
onBeforeMount(() => {
  queryTaskList();
});
const queryTaskList = () => {
  api
    .post("trade/getGoodRankPage", {
      sid: router.currentRoute.value.query.id,
    })
    .then((res) => {
      good.value = res.data.data;
    });
};
const rounding = (row, column) => {
  return parseFloat(row[column.property]).toFixed(2);
};
</script>

<style scoped></style>
