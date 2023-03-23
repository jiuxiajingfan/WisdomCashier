<template>
  <div>
    <el-form
      :inline="true"
      :model="formInline"
      style="width: 100%"
      v-loading="lod"
    >
      <el-form-item label="会员状态">
        <el-select
          v-model="formInline.status"
          multiple
          collapse-tags
          collapse-tags-tooltip
          style="width: 240px"
        >
          <el-option
            v-for="item in optionStatus"
            :key="item.value"
            :label="item.value"
            :value="item.label"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="性别">
        <el-select
          v-model="formInline.sex"
          multiple
          collapse-tags
          collapse-tags-tooltip
          style="width: 240px"
        >
          <el-option
            v-for="item in optionSex"
            :key="item.value"
            :label="item.value"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="年龄段">
        <el-select
          v-model="formInline.age"
          multiple
          collapse-tags
          collapse-tags-tooltip
          style="width: 240px"
        >
          <el-option
            v-for="item in optionAge"
            :key="item.value"
            :label="item.value"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="会员等级">
        <el-select
          v-model="formInline.level"
          multiple
          collapse-tags
          collapse-tags-tooltip
          style="width: 240px"
        >
          <el-option
            v-for="item in optionLevel"
            :key="item.label"
            :label="item.value"
            :value="item.label"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="clear">清空</el-button>
        <el-button type="primary" @click="queryTaskList">搜索</el-button>
      </el-form-item>
      <el-form-item
        ><el-button type="primary" @click="dialogFormVisible = true"
          >开始推送</el-button
        ></el-form-item
      >
    </el-form>
  </div>
  <el-table :data="good" height="calc(100vh - 280px)" v-loading="lod">
    <el-table-column type="selection" />
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
    <el-table-column prop="sex" label="性别" width="auto" />
    <el-table-column prop="age" label="年龄" width="auto" />
    <el-table-column prop="level" label="会员等级" width="auto" />
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

  <el-dialog title="文章信息" v-model="dialogFormVisible" width="60%">
    <v-md-editor v-model="form.content" height="400px"></v-md-editor>
    <h2 style="color: red">当前可用短信次数： 100</h2>
    <template #footer>
      <el-button @click="dialogFormVisible = false">取 消</el-button>
      <el-button type="primary" @click="dialogFormVisible = false"
        >推送</el-button
      >
    </template>
  </el-dialog>
</template>

<script setup>
import { onBeforeMount, reactive, ref, watch } from "vue";
import api from "@/api/api";
import router from "@/router";
let good = ref([]);
const dialogFormVisible = ref(false);
let current = ref(1);
let total = ref(0);
let form = reactive({
  name: "",
  content: "### Hello",
});
let pageSize = ref(10);
const formLabelWidth = "140px";
const queryTaskList = () => {
  lod.value = true;
  api
    .post("Shop/getVipPushPage", {
      current: current.value,
      pageSize: pageSize.value,
      sid: router.currentRoute.value.query.id,
      sex: formInline.sex,
      age: formInline.age,
      level: formInline.level,
      status: formInline.status,
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
const clear = () => {
  formInline.sex = [];
  formInline.age = [];
  formInline.status = [];
  formInline.level = [];
};
const formInline = reactive({
  status: [],
  age: [],
  sex: [],
  level: [],
});
watch(formInline, (o, n) => {
  current.value = 1;
});
const optionStatus = [
  {
    value: "有效",
    label: 1,
  },
  {
    value: "过期",
    label: 2,
  },
];
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
const optionLevel = [
  {
    value: "0星会员",
    label: 0,
  },
  {
    value: "1星会员",
    label: 1,
  },
  {
    value: "2星会员",
    label: 2,
  },
  {
    value: "3星会员",
    label: 3,
  },
  {
    value: "5星会员",
    label: 4,
  },
  {
    value: "5星会员",
    label: 5,
  },
];
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
const lod = ref(false);
</script>

<style scoped></style>
