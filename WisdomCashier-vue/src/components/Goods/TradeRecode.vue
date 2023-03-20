<template>
  <div>
    <el-form
      :inline="true"
      :model="formInline"
      style="width: 100%"
      v-loading="lod"
    >
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
      <el-table :data="trade" height="calc(100vh - 230px)" v-loading="lod">
        <el-table-column prop="id" label="流水号" width="auto" />
        <el-table-column prop="income" label="金额" width="auto" />
        <el-table-column label="交易状态" width="auto">
          <template #default="scope">
            <el-tooltip
              class="box-item"
              effect="dark"
              :content="scope.row.msg"
              placement="top"
            >
              <div style="display: flex; align-items: center">
                <i :style="getStyle(scope.row.status)"></i>
                <span style="margin-left: 2px">
                  {{ tradetype2[scope.row.status].msg }}
                </span>
              </div>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="付款方式" width="auto">
          <template #default="scope">
            <span style="margin-left: 2px">
              {{ tradetype[scope.row.type - 1] }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="交易时间" width="auto" />
        <el-table-column prop="operater" label="交易员" width="auto" />
        <el-table-column label="操作" width="auto">
          <template #default="scope">
            <el-button
              link
              type="primary"
              size="large"
              @click="querydetail(scope.row.id)"
              :disabled="scope.row.status == 2 || scope.row.status == 7"
            >
              购物详情
            </el-button>
          </template>
        </el-table-column>
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
      <el-dialog
        v-model="dialogTableVisible"
        title="消费清单"
        width="50%"
        align-center="true"
      >
        <el-table :data="detail" height="calc(30vh)">
          <el-table-column property="gid" label="商品标签号" width="auto" />
          <el-table-column property="name" label="商品名" width="auto" />
          <el-table-column property="num" label="购买数量" width="auto" />
          <el-table-column property="price" label="单价" width="auto" />
        </el-table>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import { onBeforeMount, reactive, ref, watch } from "vue";
import api from "@/api/api";
import router from "@/router";
import utils from "@/utils/utils";
let trade = ref([]);
let pageSize = ref(20);
let total = ref(0);
let dialogTableVisible = ref(false);
let detail = ref([]);
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
let lod = ref(false);
const querydetail = (row) => {
  api
    .get("trade/queryGoodsById", {
      params: {
        id: row,
      },
    })
    .then((res) => {
      if (res.data.code === 200) {
        detail.value = res.data.data;
      } else {
        utils.showMessage(400, "查询失败！");
      }
    });
  dialogTableVisible.value = true;
};
onBeforeMount(() => {
  queryTaskList();
});
const queryTaskList = () => {
  lod.value = true;
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
    })
    .finally(() => {
      lod.value = false;
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
watch(formInline, (newValue, oldValue) => {
  current.value = 1;
});
let tradetype = ["现金支付", "支付宝支付", "微信支付"];
let tradetype2 = [
  { msg: "等待付款", color: "#fffb09" },
  { msg: "失败", color: "#f60303" },
  { msg: "取消", color: "#f60303" },
  { msg: "完成可退款", color: "#409EFF" },
  { msg: "部分退款", color: "#3febfa" },
  { msg: "全额退款", color: "#ee03f6" },
  { msg: "完成不可退款", color: "#00ff14" },
  { msg: "未知错误交易停止", color: "#f60303" },
];
const getStyle = (data) => {
  return (
    "background-color: " +
    tradetype2[data].color +
    ";\n" +
    "width: 15px;\n" +
    "height: 15px;\n" +
    "border-radius: 50%;\n" +
    "display: block;"
  );
};
</script>

<style scoped></style>
