<template>
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
      >新增商品</el-button
    >
  </dev>
  <div class="table">
    <el-table :data="good">
      <el-table-column prop="name" label="商品名" width="auto" />
      <el-table-column prop="metrology" label="单位" width="auto" />
      <el-table-column prop="priceIn" label="进价" width="auto" />
      <el-table-column prop="priceOut" label="售价" width="auto" />
      <el-table-column prop="profit" label="利润" width="auto" />
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
  <el-dialog v-model="dialogFormVisible" title="新增商品" width="900px" center>
    <el-form
      :model="form"
      :inline="true"
      label-position="left"
      style="margin-left: 10%"
    >
      <div>
        <el-form-item label="商品名" :label-width="formLabelWidth">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="商品条码" :label-width="formLabelWidth">
          <el-input v-model="form.gid" />
        </el-form-item>
      </div>
      <div>
        <el-form-item label="进价" :label-width="formLabelWidth">
          <el-input v-model="form.price_in" />
        </el-form-item>
        <el-form-item label="售价" :label-width="formLabelWidth">
          <el-input v-model="form.price_out" />
        </el-form-item>
      </div>
      <div>
        <el-form-item label="数量" :label-width="formLabelWidth">
          <el-input v-model="form.num" />
        </el-form-item>
        <el-form-item label="单位" :label-width="formLabelWidth">
          <el-input v-model="form.metrology" />
        </el-form-item>
      </div>
      <div>
        <el-form-item label="生产日期" :label-width="formLabelWidth">
          <el-input v-model="form.date" />
        </el-form-item>
        <el-form-item label="保质期(天)" :label-width="formLabelWidth">
          <el-input v-model="form.shelfLife" />
        </el-form-item>
      </div>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">离开</el-button>
        <el-button type="primary" @click="dialogFormVisible = false">
          保存
        </el-button>
      </span>
    </template>
  </el-dialog>
  <el-dialog
    v-model="dialogFormVisible2"
    title="未查询到商品"
    width="600px"
    center
  >
    <h1>未找到该商品信息，是否需要添加该商品？</h1>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible2 = false"> 离开 </el-button>
        <el-button
          type="primary"
          @click="
            dialogFormVisible = true;
            dialogFormVisible2 = false;
          "
          >添加</el-button
        >
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { onBeforeMount, reactive, ref } from "vue";
import api from "@/api/api";
import { useRouter } from "vue-router/dist/vue-router";
const dialogFormVisible = ref(false);
const dialogFormVisible2 = ref(false);
const formLabelWidth = "140px";
let good = ref([]);
let current = ref(1);
let total = ref(0);
let pageSize = ref(10);
let searchText = ref("");
let currentText = "";
let sid = ref(1);
var reg = /^[0-9]*$/;
const router = useRouter();
const form = reactive({
  name: "",
  metrology: "",
  gid: "",
  price_in: "",
  price_out: "",
  sid: router.currentRoute.value.query.id,
  date: "",
  profit: "",
  shelfLife: "",
  num: "",
});
onBeforeMount(() => {
  queryTaskList();
});
const queryTaskList = () => {
  sid.value = router.currentRoute.value.query.id;
  if (currentText != searchText.value) {
    current.value = 1;
    currentText = searchText.value;
  }
  api
    .post("Goods/getGoodPage", {
      current: current.value,
      pageSize: pageSize.value,
      gid: searchText.value,
      sid: router.currentRoute.value.query.id,
    })
    .then((res) => {
      good.value = res.data.data.records;
      current.value = res.data.data.current;
      total.value = res.data.data.total;
      if (good.value.length === 0) dialogFormVisible2.value = true;
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
const openadd = () => {
  if (reg.test(searchText.value)) {
    form.gid = searchText.value;
  } else {
    form.name = searchText.value;
  }
  dialogFormVisible.value = true;
};
</script>

<style scoped lang="scss">
.searchBox {
  .el-input {
    width: 60%;
  }
}
</style>
