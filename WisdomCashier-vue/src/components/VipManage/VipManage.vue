<template>
  <el-row>
    <el-col :span="24">
      <h2 style="font-size: 30px">会员详情</h2>
    </el-col>
  </el-row>
  <dev class="searchBox" style="width: 100%; text-align: center">
    <el-input
      placeholder="请扫描需要查询的会员卡或输入手机号"
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
      >新增会员
    </el-button>
  </dev>
  <div class="table">
    <el-table :data="good" height="calc(100vh - 280px)" v-loading="lod">
      <el-table-column prop="id" label="会员号" width="auto" />
      <el-table-column label="状态" width="auto">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <i :style="getStyle(scope.row.status - 1)"></i>
            <span style="margin-left: 2px">
              {{ tradetype2[scope.row.status - 1].msg }}
            </span>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="limit" label="过期时间" width="auto" />
      <el-table-column prop="integration" label="积分" width="auto" />
      <el-table-column prop="level" label="会员等级" width="auto" />

      <el-table-column label="操作">
        <template #default="scope">
          <el-button
            link
            type="primary"
            size="large"
            @click="change(scope.row.id)"
          >
            会员续期
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
    <el-dialog v-model="dialogVisiblezfb" title="新增会员" width="30%">
      <el-form :model="form" label-position="left" style="margin-left: 10%">
        <div>
          <el-form-item label="手机号" :label-width="formLabelWidth">
            <el-input style="width: 190px" v-model="form.phone" />
          </el-form-item>
          <el-form-item label="性别" :label-width="formLabelWidth">
            <el-select
              style="width: 190px"
              v-model="form.sex"
              placeholder="Select"
            >
              <el-option
                v-for="item in optionSex"
                :key="item.value"
                :label="item.value"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </div>
        <div>
          <el-form-item label="年龄分类" :label-width="formLabelWidth">
            <el-select
              style="width: 190px"
              v-model="form.age"
              placeholder="Select"
            >
              <el-option
                v-for="item in optionAge"
                :key="item.value"
                :label="item.value"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </div>
        <div>
          <el-form-item label="会员有效期" :label-width="formLabelWidth">
            <el-select
              style="width: 190px"
              v-model="form.limit"
              placeholder="Select"
            >
              <el-option
                v-for="item in optionLimit"
                :key="item.value"
                :label="item.value"
                :value="item.label"
              />
            </el-select>
          </el-form-item>
        </div>
      </el-form>
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
      title="会员续期"
      width="50%"
      align-center="true"
    >
      <h2>请选择续期时间</h2>
      <el-select style="width: 190px" v-model="form.limit" placeholder="Select">
        <el-option
          v-for="item in optionLimit"
          :key="item.value"
          :label="item.value"
          :value="item.label"
        />
      </el-select>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisiblezfb = false">取消</el-button>
          <el-button type="primary" @click="addEmploree4" :loading="lod">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onBeforeMount, reactive, ref } from "vue";
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
let lod = ref(false);
let currentText = "";
const type = ref();
const change = (res) => {
  type.value = res;
  dialogTableVisible.value = true;
};
const form = reactive({
  phone: "",
  sex: "",
  age: "",
  sid: router.currentRoute.value.query.id,
  limit: "",
});
const optionSex = [
  {
    value: "男",
  },
  {
    value: "女",
  },
];
const optionAge = [
  {
    value: "18-25",
  },
  {
    value: "26-35",
  },
  {
    value: "35-60",
  },
  {
    value: "60岁以上",
  },
];
const optionLimit = [
  {
    value: "一个月",
    label: 1,
  },
  {
    value: "三个月",
    label: 3,
  },
  {
    value: "半年",
    label: 6,
  },
  {
    value: "一年",
    label: 12,
  },
  {
    value: "两年",
    label: 24,
  },
  {
    value: "无期限",
    label: 1000,
  },
];
const formLabelWidth = "140px";
const queryTaskList = () => {
  lod.value = true;
  if (currentText != searchText.value) {
    current.value = 1;
    currentText = searchText.value;
  }
  api
    .post("Shop/getVipPage", {
      current: current.value,
      pageSize: pageSize.value,
      sid: router.currentRoute.value.query.id,
      gid: searchText.value.trim(),
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
const addEmploree = () => {
  lod.value = true;
  api
    .post("Shop/addVip", {
      sid: router.currentRoute.value.query.id,
      phone: form.phone.trim(),
      age: form.age,
      sex: form.sex,
      limit: form.limit,
    })
    .then((res) => {
      utils.showMessage(res.data.code, res.data.msg);
      queryTaskList();
      form.phone = "";
      form.sex = "";
      form.limit = "";
      form.age = "";
    })
    .finally(() => {
      lod.value = false;
      dialogVisiblezfb.value = false;
    });
};
const addEmploree4 = () => {
  lod.value = true;
  api
    .post("Shop/renewalVip", {
      sid: router.currentRoute.value.query.id,
      phone: type.value,
      limit: form.limit,
    })
    .then((res) => {
      utils.showMessage(res.data.code, res.data.msg);
      queryTaskList();
    })
    .finally(() => {
      form.limit = "";
      lod.value = false;
    });
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
let tradetype2 = [
  { msg: "有效", color: "#07f500" },
  { msg: "过期", color: "#f60303" },
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

<style scoped lang="scss">
.searchBox {
  .el-input {
    width: 60%;
  }
}
</style>
