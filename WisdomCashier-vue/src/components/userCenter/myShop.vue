<template>
  <p class="ff">我的店铺</p>
  <div class="searchBox">
    <el-input
      style="width: 80%"
      placeholder="请输入要搜索的店铺名"
      v-model="searchText"
      class="input-with-select"
      @keyup.enter="queryTaskList"
    >
      <template #append>
        <el-button icon="Search" @click="queryTaskList" />
      </template>
    </el-input>
    <el-button
      style="margin-left: 30px"
      type="primary"
      @click="dialogTableVisible = true"
      >申请加入</el-button
    >
  </div>
  <div class="mm">
    <div class="tables">
      <el-scrollbar>
        <el-table
          :data="shops"
          height="calc(100vh - 230px)"
          style="width: 100%"
          :show-header="true"
          empty-text="暂无店铺，快去申请或加入吧！"
        >
          <el-table-column prop="shopName" label="店铺名" width="auto" />
          <el-table-column label="操作" align="right">
            <template v-slot="scope">
              <el-button
                link
                type="primary"
                size="large"
                @click="go(scope.row.id)"
              >
                进入
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-scrollbar>
      <div class="page">
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
        ></el-pagination>
      </div>
    </div>
  </div>
  <el-dialog
    v-model="dialogTableVisible"
    title="申请列表"
    width="50%"
    align-center="true"
  >
    <el-input
      style="width: 80%"
      placeholder="请输入要加入的店铺ID"
      v-model="searchText2"
      class="input-with-select"
      @keyup.enter="queryTaskList2"
    >
      <template #append>
        <el-button icon="Search" @click="queryTaskList2" />
      </template>
    </el-input>
    <el-table :data="applyData" height="calc(30vh)">
      <el-table-column property="name" label="店铺ID" width="auto" />
      <el-table-column label="申请状态" width="auto">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <i :style="getStyle(scope.row.userId - 1)"></i>
            <span style="margin-left: 2px">
              {{ tradetype2[scope.row.userId - 1].msg }}
            </span>
          </div>
        </template>
      </el-table-column>
      <el-table-column property="gmtCreate" label="申请时间" width="auto" />
    </el-table>
  </el-dialog>
</template>
<script setup>
import { onBeforeMount, ref } from "vue";
import api from "@/api/api";
const shops = ref([]);
const dialogTableVisible = ref(false);
let current = ref(1);
let total = ref(0);
let pageSize = ref(20);
let searchText = ref("");
let searchText2 = ref("");
let currentText = "";
import { useRouter } from "vue-router";
import utils from "@/utils/utils";
const router = useRouter();
onBeforeMount(() => {
  api
    .post("shop/getUserShopPage", {
      current: 1,
      pageSize: 20,
      name: searchText.value,
    })
    .then((res) => {
      shops.value = res.data.data.records;
      current.value = res.data.data.current;
      total.value = res.data.data.total;
    });
  queryTaskList3();
});
const lod = ref(false);
const applyData = ref([]);
const queryTaskList3 = () => {
  api.post("shop/getApplyListPer").then((res) => {
    applyData.value = res.data.data;
  });
};
const queryTaskList2 = () => {
  lod.value = true;
  api
    .get("shop/applyShop", {
      params: {
        sid: searchText2.value.trim(),
      },
    })
    .then((res) => {
      utils.showMessage(res.data.code, res.data.msg);
      if (res.data.code === 200) {
        searchText2.value = "";
        queryTaskList3();
      }
    })
    .finally(() => {
      lod.value = true;
    });
};
const queryTaskList = () => {
  if (currentText != searchText.value) {
    current.value = 1;
    currentText = searchText.value;
  }
  api
    .post("shop/getUserShopPage", {
      current: current.value,
      pageSize: pageSize.value,
      name: searchText.value,
    })
    .then((res) => {
      shops.value = res.data.data.records;
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
const go = (id) => {
  router.push({
    path: "/charge",
    query: {
      id: id,
    },
  });
};
let tradetype2 = [
  { msg: "待审批", color: "#fffb09" },
  { msg: "通过", color: "#00ff08" },
  { msg: "拒绝", color: "#ff0000" },
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
.mm {
  background-color: #ffffff;
}
.page {
  float: contour;
}
.ff {
  font-family: "Google Sans", Roboto, Arial, sans-serif;
  line-height: 2.25rem;
  font-size: 1.75rem;
  letter-spacing: 0;
  font-weight: 400;
  -webkit-hyphens: auto;
  hyphens: auto;
  word-break: break-word;
  word-wrap: break-word;
  color: var(--gm3-sys-color-on-background, #1f1f1f);
  text-align: center;
}
</style>
