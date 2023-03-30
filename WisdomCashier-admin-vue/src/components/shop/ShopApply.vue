<template>
  <el-row>
    <el-col :span="24">
      <h2 style="font-size: 30px">申请列表</h2>
    </el-col>
  </el-row>
  <div class="table">
    <el-table
      :data="good"
      height="calc(100vh - 280px)"
      v-loading="lod"
      empty-text="暂无申请"
    >
      <el-table-column type="expand">
        <template #default="props">
          <el-row>
            <el-col :span="8">
              <el-card>
                <template #header>
                  <h2>营业执照</h2>
                </template>
                <el-image
                  :src="props.row.img[0]"
                  style="width: 400px; height: 800px"
                ></el-image>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card>
                <template #header>
                  <h2>身份证正面</h2>
                </template>
                <el-image
                  :src="props.row.img[1]"
                  style="width: 400px; height: 600px"
                ></el-image>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card>
                <template #header>
                  <h2>身份证反面</h2>
                </template>
                <el-image
                  :src="props.row.img[2]"
                  style="width: 400px; height: 600px"
                ></el-image>
              </el-card>
            </el-col>
          </el-row>
        </template>
      </el-table-column>
      <el-table-column prop="gmtCreate" label="申请时间" width="auto" />
      <el-table-column prop="name" label="店铺名" width="auto" />
      <el-table-column prop="code" label="社会同一信用代码" width="auto" />
      <el-table-column label="申请状态" width="auto">
        <template #default="scope">
          <span>
            {{ tradetype[scope.row.status - 1] }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="operator" label="操作人" width="auto" />
      <el-table-column prop="tips" label="说明" width="auto" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button
            link
            type="primary"
            size="large"
            @click="change(scope.row.status, scope.row.id)"
            v-show="scope.row.status === 1"
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
      <div v-show="type === 3">
        <h2>请输入原因</h2>
        <el-input v-model="msg" type="textarea"></el-input>
      </div>
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
  dialogVisiblezfb2.value = true;
};
const options = [
  {
    value: 2,
    label: "通过",
  },
  {
    value: 3,
    label: "拒绝",
  },
];
const queryTaskList = () => {
  lod.value = true;
  if (currentText != searchText.value) {
    current.value = 1;
    currentText = searchText.value;
  }
  api
    .post("shop/getApplyPage", {
      current: current.value,
      pageSize: pageSize.value,
    })
    .then((res) => {
      good.value = res.data.data.records;
      current.value = res.data.data.current;
      total.value = res.data.data.total;
    })
    .finally(() => {
      lod.value = false;
      msg.value = "";
    });
};
const msg = ref("");
const addEmploree3 = () => {
  lod.value = true;
  api
    .post("shop/changeApplyStatus", {
      type: type.value,
      id: type2.value,
      msg: msg.value,
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
let tradetype = ["待审批", "通过", "拒绝"];
</script>

<style scoped lang="scss">
.searchBox {
  .el-input {
    width: 60%;
  }
}
</style>
