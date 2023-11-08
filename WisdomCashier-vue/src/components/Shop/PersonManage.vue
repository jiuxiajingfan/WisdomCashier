<template>
  <el-row>
    <el-col :span="24">
      <h2 style="font-size: 30px">人员管理</h2>
    </el-col>
  </el-row>
  <dev class="searchBox" style="width: 100%; text-align: center">
    <el-input
      placeholder="请输入要搜索的员工姓名"
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
    <el-badge :value="apply.length" :hidden="apply.length === 0">
      <el-button
        style="margin-left: 20px"
        icon="Document"
        type="primary"
        @click="dialogTableVisible = true"
        >申请列表
      </el-button>
    </el-badge>
  </dev>
  <div class="table">
    <el-table :data="good" height="calc(100vh - 280px)" v-loading="lod">
      <el-table-column prop="pic" label="头像">
        <template v-slot="scope">
          <el-image
            :src="scope.row.image + '?' + new Date().getTime()"
            style="width: 60px; height: 60px"
          >
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="id" label="账号ID" width="auto" />
      <el-table-column prop="userNickname" label="姓名" width="auto" />
      <el-table-column prop="phone" label="电话" width="auto" />
      <el-table-column prop="email" label="邮箱" width="auto" />
      <el-table-column label="用户权限" width="auto">
        <template #default="scope">
          <span>
            {{ tradetype[scope.row.roleEnum - 1] }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button
            link
            type="primary"
            size="large"
            @click="change(scope.row.roleEnum, scope.row.id)"
            :disabled="scope.row.roleEnum === 1"
          >
            更改权限
          </el-button>
          <el-popconfirm
            title="确定要删除吗?"
            @confirm="addEmploree4(scope.row.id)"
          >
            <template #reference>
              <el-button
                link
                type="primary"
                :disabled="scope.row.roleEnum === 1"
                >删除</el-button
              >
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
        @keyup.enter="addEmploree"
      ></el-input>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisiblezfb = false">取消</el-button>
          <el-button type="primary" @click="addEmploree" :loading="lod">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog
      v-model="dialogTableVisible"
      title="申请列表"
      width="50%"
      align-center="true"
    >
      <el-table :data="apply" height="calc(30vh)" v-loading="lod">
        <el-table-column property="userId" label="用户ID" width="auto" />
        <el-table-column property="name" label="姓名" width="auto" />
        <el-table-column property="phone" label="电话" width="auto" />
        <el-table-column property="gmtCreate" label="申请时间" width="auto" />
        <el-table-column label="操作">
          <template #default="scope">
            <el-button
              link
              type="primary"
              size="large"
              @click="addEmploree2(scope.row.userId, 3)"
            >
              拒绝
            </el-button>
            <el-popconfirm
              title="确定要通过审批吗?"
              @confirm="addEmploree2(scope.row.userId, 2)"
            >
              <template #reference>
                <el-button link type="primary">通过</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
    <el-dialog v-model="dialogVisiblezfb2" title="更改权限" width="30%">
      <h2 style="font-size: 30px">设置权限为：</h2>
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
import router from "@/router";
import utils from "@/utils/utils";
let dialogTableVisible = ref(false);
let good = ref([]);
let current = ref(1);
let total = ref(0);
let pageSize = ref(10);
let searchText = ref("");
let dialogVisiblezfb = ref(false);
let dialogVisiblezfb2 = ref(false);
let lod = ref(false);
let currentText = "";
let userPayID = ref("");
let apply = ref([]);
const type = ref();
const type2 = ref("");
const change = (res, res2) => {
  type2.value = res2;
  type.value = res;
  dialogVisiblezfb2.value = true;
};
const options = [
  {
    value: 2,
    label: "店铺管理员",
  },
  {
    value: 3,
    label: "收银员",
  },
];
const queryTaskList2 = () => {
  api
    .get("shop/getApplyList", {
      params: {
        sid: router.currentRoute.value.query.id,
      },
    })
    .then((res) => {
      apply.value = res.data.data;
    });
};
const queryTaskList = () => {
  lod.value = true;
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
    })
    .finally(() => {
      lod.value = false;
    });
};
const addEmploree2 = (res, res2) => {
  lod.value = true;
  api
    .post("Shop/approval", {
      sid: router.currentRoute.value.query.id,
      pid: res,
      type: res2,
    })
    .then((res) => {
      utils.showMessage(res.data.code, res.data.msg);
      queryTaskList2();
      queryTaskList();
    })
    .finally(() => {
      lod.value = false;
    });
};
const addEmploree3 = () => {
  lod.value = true;
  api
    .post("Shop/changeRole", {
      sid: router.currentRoute.value.query.id,
      pid: type2.value,
      type: type.value,
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
const addEmploree = () => {
  lod.value = true;
  api
    .get("Shop/addEmploree", {
      params: {
        sid: router.currentRoute.value.query.id,
        pid: userPayID.value.trim(),
      },
    })
    .then((res) => {
      utils.showMessage(res.data.code, res.data.msg);
      queryTaskList();
    })
    .finally(() => {
      lod.value = false;
      dialogVisiblezfb.value = false;
      userPayID.value = "";
    });
};
const addEmploree4 = (res) => {
  lod.value = true;
  api
    .post("Shop/deleteEmploree", {
      sid: router.currentRoute.value.query.id,
      id: res,
    })
    .then((res) => {
      utils.showMessage(res.data.code, res.data.msg);
      queryTaskList();
    })
    .finally(() => {
      lod.value = false;
    });
};
onBeforeMount(() => {
  queryTaskList();
  queryTaskList2();
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
let tradetype = ["店长", "店铺管理员", "收银员"];
</script>

<style scoped lang="scss">
.searchBox {
  .el-input {
    width: 60%;
  }
}
</style>
