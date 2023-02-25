<template>
  <dev class="searchBox">
    <el-input
      placeholder="请输入要搜索的店铺名"
      v-model="searchText"
      class="input-with-select"
      @keyup.enter="queryTaskList"
    >
      <template #append>
        <el-button icon="Search" @click="queryTaskList" />
      </template>
    </el-input>
  </dev>
  <div class="mm">
    <div class="tables">
      <el-scrollbar>
        <el-table :data="shops" style="width: 100%" :show-header="true">
          <el-table-column prop="shopName" label="店铺名" width="auto" />
          <el-table-column label="操作" align="right">
            <template v-slot="scope">
              <el-button
                link
                type="primary"
                size="large"
                v-if="scope.row.role < 3"
              >
                管理
              </el-button>
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
</template>
<script setup>
import { onBeforeMount, ref } from "vue";
import api from "@/api/api";
const shops = ref([]);
let current = ref(1);
let total = ref(0);
let pageSize = ref(10);
let searchText = ref("");
let currentText = "";
import { useRouter } from "vue-router";
const router = useRouter();
onBeforeMount(() => {
  api
    .post("Shop/getUserShopPage", {
      current: 1,
      pageSize: 10,
      name: searchText.value,
    })
    .then((res) => {
      shops.value = res.data.data.records;
      current.value = res.data.data.current;
      total.value = res.data.data.total;
    });
});
const queryTaskList = () => {
  if (currentText != searchText.value) {
    current.value = 1;
    currentText = searchText.value;
  }
  api
    .post("Shop/getUserShopPage", {
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
    path: "shop",
    params: {
      id: id,
    },
  });
};
</script>

<style scoped lang="scss">
.mm {
  background-color: #ffffff;
}
.page {
  float: contour;
}
</style>
