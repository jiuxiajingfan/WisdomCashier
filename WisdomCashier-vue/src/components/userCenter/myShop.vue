<template>
  <div class="mm">
    <div class="tables">
      <el-table :data="shops" style="width: 50%" :show-header="true" stripe>
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
            <el-button link type="primary" size="large"> 进入 </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { onBeforeMount, ref } from "vue";
import api from "@/api/api";

const shops = ref([]);
onBeforeMount(() => {
  api.post("Shop/getUserShopPage").then((res) => {
    shops.value = res.data.data.records;
  });
});
</script>

<style scoped lang="scss">
.mm {
  background-color: #ffffff;
}
</style>
