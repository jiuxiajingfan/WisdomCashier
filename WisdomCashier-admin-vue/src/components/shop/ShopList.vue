<template>
  <el-row>
    <el-col :span="24">
      <h2 style="font-size: 30px">店铺管理</h2>
    </el-col>
  </el-row>
  <dev class="searchBox" style="width: 100%; text-align: center">
    <el-input
      placeholder="请输入要搜索的店铺ID"
      v-model="searchText"
      class="input-with-select"
      @keyup.enter="queryTaskList"
    >
      <template #append>
        <el-button icon="Search" @click="queryTaskList">搜索</el-button>
      </template>
    </el-input>
  </dev>
  <div class="table">
    <el-table :data="good" height="calc(100vh - 280px)" v-loading="lod">
      <el-table-column prop="pic" label="营业执照">
        <template v-slot="scope">
          <el-image
            :src="scope.row.desc + '?' + new Date().getTime()"
            style="width: 60px; height: 60px"
          >
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="id" label="店铺ID" width="auto" />
      <el-table-column prop="shopName" label="店铺名" width="auto" />
      <el-table-column prop="createTime" label="创建时间" width="auto" />
      <el-table-column label="店铺状态" width="auto">
        <template #default="scope">
          <span>
            {{ tradetype[scope.row.role] }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button
            link
            type="primary"
            size="large"
            @click="change(scope.row.role, scope.row.id)"
          >
            更改状态
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
    <el-dialog v-model="dialogVisiblezfb2" title="更改状态" width="30%">
      <h2 style="font-size: 30px">设置状态为：</h2>
      <el-select v-model="type" placeholder="Select" default-first-option="1">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
          :disabled="item.disabled"
        />
      </el-select>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisiblezfb2 = false">取消</el-button>
          <el-button type="primary" @click="addEmploree3" :loading="lod">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onBeforeMount, ref } from "vue";
import api from "@/api/api";
import utils from "@/utils/utils";
let good = ref([]);
let current = ref(1);
let total = ref(0);
let pageSize = ref(10);
let searchText = ref("");
let dialogVisiblezfb2 = ref(false);
let lod = ref(false);
let currentText = "";
const type = ref();
const type2 = ref("");
const change = (res, res2) => {
  type2.value = res2;
  type.value = res;
  dialogVisiblezfb2.value = true;
};
const options = [
  {
    value: 0,
    label: "有效",
  },
  {
    value: 1,
    label: "封禁",
  },
  {
    value: 2,
    label: "注销",
  },
];
const queryTaskList = () => {
  lod.value = true;
  if (currentText != searchText.value) {
    current.value = 1;
    currentText = searchText.value;
  }
  api
    .post("shop/getShopPage", {
      current: current.value,
      pageSize: pageSize.value,
      name: searchText.value.trim(),
    })
    .then((res) => {
      good.value = res.data.data.records;
      current.value = res.data.data.current;
      total.value = res.data.data.total;
    })
    .finally(() => {
      lod.value = false;
    });
};
const addEmploree3 = () => {
  lod.value = true;
  api
    .post("shop/changeShopStatus", {
      sid: type.value,
      id: type2.value,
    })
    .then((res) => {
      utils.showMessage(res.data.code, res.data.msg);
      queryTaskList();
      dialogVisiblezfb2.value = false;
    })
    .finally(() => {
      lod.value = false;
    });
};
onBeforeMount(() => {
  queryTaskList();
});
const taskCurrentChange = (cnt) => {
  current.value = cnt;
  queryTaskList();
};
const taskSizeChange = (ps) => {
  pageSize.value = ps;
  queryTaskList();
};
let tradetype = ["有效", "封禁", "注销"];
</script>

<style scoped lang="scss">
.searchBox {
  .el-input {
    width: 60%;
  }
}
</style>
