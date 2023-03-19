<template>
  <el-row>
    <el-col :span="24">
      <h2 style="font-size: 30px">人员管理</h2>
    </el-col>
  </el-row>
  <dev class="searchBox" style="width: 100%; text-align: center">
    <el-input
      placeholder="请输入要搜索的商品名或条形码"
      v-model="searchText"
      class="input-with-select"
      @keyup.enter="queryTaskList"
    >
      <template #append>
        <el-button icon="Search" @click="queryTaskList">搜索</el-button>
      </template>
    </el-input>
    <el-button
      style="margin-left: 20px"
      icon="Plus"
      type="primary"
      @click="openadd"
      >新增员工
    </el-button>
    <el-button
      style="margin-left: 20px"
      icon="Plus"
      type="primary"
      @click="openadd"
      >申请列表
    </el-button>
  </dev>
  <div class="table">
    <el-table :data="good" height="calc(100vh - 230px)">
      <el-table-column prop="pic" label="头像">
        <template v-slot="scope">
          <el-image :src="scope.row.image" style="width: 60px; height: 60px">
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="id" label="账号ID" width="auto" />
      <el-table-column prop="userNickname" label="姓名" width="auto" />
      <el-table-column prop="phone" label="电话" width="auto" />
      <el-table-column prop="email" label="邮箱" width="auto" />
      <el-table-column prop="roleEnum" label="用户权限" width="auto" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button
            link
            type="primary"
            size="large"
            @click="updateGood(scope.row)"
          >
            更新
          </el-button>
          <el-popconfirm
            title="确定要删除吗?"
            @confirm="deleteGood(scope.row.gid)"
          >
            <template #reference>
              <el-button link type="primary">删除</el-button>
            </template>
          </el-popconfirm>
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
      v-model="dialogVisiblezfb"
      title="新增员工"
      width="30%"
      @focus="this.$refs['zfbinput'].focus()"
    >
      <h2 style="font-size: 30px">请输入员工账号ID</h2>
      <el-input
        ref="zfbinput"
        v-model="userPayID"
        @keyup.enter="alipayP"
      ></el-input>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisiblezfb = false">取消</el-button>
          <el-button type="primary" @click="addEmploree"> 确定 </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onBeforeMount, ref } from "vue";
import api from "@/api/api";
import router from "@/router";
let good = ref([]);
let current = ref(1);
let total = ref(0);
let pageSize = ref(10);
let searchText = ref("");
let dialogVisiblezfb = ref(false);
let currentText = "";
let userPayID = ref("");
const queryTaskList = () => {
  if (currentText != searchText.value) {
    current.value = 1;
    currentText = searchText.value;
  }
  api
    .post("Shop/getEmploree", {
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
const addEmploree = () =>{
  console.log(1); 
};
onBeforeMount(() => {
  queryTaskList();
});
const openadd = () => {
  dialogVisiblezfb.value = true;
};
const taskCurrentChange = (cnt) => {
  current.value = cnt;
  queryTaskList();
};
const taskSizeChange = (ps) => {
  pageSize.value = ps;
  queryTaskList();
};
</script>

<style scoped lang="scss">
.searchBox {
  .el-input {
    width: 60%;
  }
}
</style>
