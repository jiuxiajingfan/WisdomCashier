<template>
  <div>
    <el-form :inline="true" :model="formInline" style="width: 100%">
      <el-form-item label="交易流水号">
        <el-input v-model="formInline.id" />
      </el-form-item>
      <el-form-item label="交易日期">
        <el-date-picker
          v-model="formInline.date"
          type="daterange"
          unlink-panels
          range-separator="至"
          start-placeholder="起始日期"
          end-placeholder="结束日期"
          :shortcuts="shortcuts"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>
      <el-form-item label="交易结果">
        <el-select
          v-model="formInline.type"
          multiple
          collapse-tags
          collapse-tags-tooltip
          style="width: 240px"
        >
          <el-option
            v-for="item in tradetype1"
            :key="item.color"
            :label="item.msg"
            :value="item.color"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="clear">清空</el-button>
        <el-button type="primary" @click="queryTaskList">搜索</el-button>
      </el-form-item>
    </el-form>
  </div>
  <div>
    <div class="table">
      <el-table :data="trade" height="calc(100vh - 230px)">
        <el-table-column prop="id" label="流水号" width="auto" />
        <el-table-column prop="income" label="金额" width="auto" />
        <el-table-column prop="status" label="交易状态" width="auto" />
        <el-table-column prop="type" label="付款方式" width="auto" />
        <el-table-column prop="createTime" label="交易时间" width="auto" />
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
  </div>
</template>

<script setup>
import { onBeforeMount, reactive, ref } from "vue";
import api from "@/api/api";
import router from "@/router";
let trade = ref([]);
let pageSize = ref(20);
let total = ref(0);
const clear = () => {
  formInline.id = "";
  formInline.date = null;
  formInline.type = [];
};
const formInline = reactive({
  id: "",
  date: "",
  type: [],
});
onBeforeMount(() => {
  queryTaskList();
});
const queryTaskList = () => {
  api
    .post("trade/queryTradePage", {
      id: formInline.id,
      status: formInline.type,
      current: current.value,
      pageSize: pageSize.value,
      sid: router.currentRoute.value.query.id,
      startTime: formInline.date === null ? null : formInline.date[0],
      endTime: formInline.date === null ? null : formInline.date[1],
    })
    .then((res) => {
      trade.value = res.data.data.records;
      current.value = res.data.data.current;
      total.value = res.data.data.total;
    });
};
let current = ref(1);
const shortcuts = [
  {
    text: "本星期",
    value: () => {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
      return [start, end];
    },
  },
  {
    text: "本月",
    value: () => {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
      return [start, end];
    },
  },
  {
    text: "最近三个月",
    value: () => {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
      return [start, end];
    },
  },
];
const tradetype1 = [
  { msg: "等待付款", color: 0 },
  { msg: "失败", color: 1 },
  { msg: "取消", color: 2 },
  { msg: "完成可退款", color: 3 },
  { msg: "部分退款", color: 4 },
  { msg: "全额退款", color: 5 },
  { msg: "完成不可退款", color: 6 },
  { msg: "未知错误交易停止", color: 7 },
];
const taskCurrentChange = (cnt) => {
  current.value = cnt;
  queryTaskList();
};
const taskSizeChange = (ps) => {
  pageSize.value = ps;
  queryTaskList();
};
</script>

<style scoped></style>
