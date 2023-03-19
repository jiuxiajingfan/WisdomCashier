<template>
  <div class="table">
    <h2>请核验商品后及时前往商品列表修改商品信息</h2>
    <el-table
      :data="good"
      height="calc(100vh - 230px)"
      empty-text="暂无即将过期产品"
    >
      <el-table-column prop="gid" label="商品条码" width="auto" />
      <el-table-column prop="name" label="商品名" width="auto" />
      <el-table-column prop="metrology" label="单位" width="auto" />
      <el-table-column
        prop="priceIn"
        label="进价"
        width="auto"
        :formatter="rounding"
      />
      <el-table-column
        prop="priceOut"
        label="售价"
        width="auto"
        :formatter="rounding"
      />
      <el-table-column
        prop="profit"
        label="利润"
        width="auto"
        :formatter="rounding"
      />
      <el-table-column prop="num" label="数量" width="auto" />
      <el-table-column prop="deadline" label="过期时间" width="auto" />
    </el-table>
    <el-pagination
      :hide-on-single-page="false"
      layout="->, total, prev, pager, next, sizes"
      v-model:page-size="pageSize"
      :page-sizes="[10, 20, 50, 100]"
      background
      :total="total"
      :pager-count="7"
      @current-change="taskCurrentChange"
      @size-change="taskSizeChange"
    >
    </el-pagination>
  </div>
</template>

<script setup>
import { onBeforeMount, ref } from "vue";
import api from "@/api/api";
import router from "@/router";

let good = ref([]);
let current = ref(1);
let total = ref(0);
let pageSize = ref(20);
onBeforeMount(() => {
  queryTaskList();
});
const queryTaskList = () => {
  api
    .post("Goods/getGoodTemporaryPage", {
      current: current.value,
      pageSize: pageSize.value,
      sid: router.currentRoute.value.query.id,
    })
    .then((res) => {
      good.value = res.data.data.records;
      current.value = res.data.data.current;
      total.value = res.data.data.total;
    });
};
const taskCurrentChange = (cnt) => {
  current.value = cnt;
  queryTaskList();
};
const taskSizeChange = (ps) => {
  pageSize.value = ps;
  queryTaskList();
};
const rounding = (row, column) => {
  return parseFloat(row[column.property]).toFixed(2);
};
</script>

<style scoped></style>
