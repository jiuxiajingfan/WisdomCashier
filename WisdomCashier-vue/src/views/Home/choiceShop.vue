<template>
  <div class="back">
    <div class="header">
      <el-menu
        :default-active="activeIndex"
        class="el-menu-demo"
        mode="horizontal"
        :ellipsis="false"
        @select="handleSelect"
      >
        <div class="flex-grow" />
        <el-sub-menu index="2">
          <template #title>用户中心</template>
          <el-menu-item index="2-1">item one</el-menu-item>
          <el-menu-item index="2-2">item two</el-menu-item>
          <el-menu-item index="2-3">item three</el-menu-item>
          <el-sub-menu index="2-4">
            <template #title>item four</template>
            <el-menu-item index="2-4-1">item one</el-menu-item>
            <el-menu-item index="2-4-2">item two</el-menu-item>
            <el-menu-item index="2-4-3">item three</el-menu-item>
          </el-sub-menu>
        </el-sub-menu>
      </el-menu>
    </div>
    <div class="title">
      <h1>请选择店铺</h1>
    </div>
    <div class="box">
      <dev class="searchBox">
        <el-input
          placeholder="请输入要搜索的店铺名"
          v-model="searchText"
          class="input-with-select"
          @keyup.enter="searchShop"
        >
          <template #append>
            <el-button icon="Search" @click="searchShop" />
          </template>
        </el-input>
      </dev>
      <br />
      <br />
      <br />
      <div class="table">
        <el-table
          :data="shops"
          style="width: 100%"
          :show-header="false"
          :height="tableheight"
        >
          <el-table-column label="Operations" align="center">
            <template v-slot="scope">
              <el-button link type="primary" size="large">
                {{ scope.row.shopName }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from "vue";
import { ref } from "vue";
import api from "@/api/api";
const shops = ref([]);
const tableheight = document.documentElement.clientHeight * 0.32;
const searchText = ref("");
onMounted(() => {
  api.get("choiceShop/getUserShop").then((res) => {
    shops.value = res.data.data;
  });
});
const searchShop = () => {
  console.log(searchText.value);
  api
    .get("choiceShop/getUserShop", {
      params: {
        shopName: searchText.value,
      },
    })
    .then((res) => {
      shops.value = res.data.data;
    });
};
</script>

<style scoped lang="scss">
.back {
  background: url("../../../public/img/12.jpg") no-repeat;
  background-position: center;
  height: 100%;
  width: 100%;
  background-size: cover;
  position: fixed;
  margin: 0;
}

.title {
  color: #ecf3ed;
  position: absolute;
  top: 10%;
  left: 50%;
  margin-left: -150px;
  height: 100px;
  width: 305px;
  text-align: center;
  font-size: 25px;
}

.box {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 60%;
  padding: 40px;
  box-sizing: border-box;
  box-shadow: 0 15px 25px rgba(0, 0, 0, 0.5);
  border-radius: 30px;
  height: 50%;

  .table {
    .el-table {
      background-color: transparent !important;
      --el-table-tr-bg-color: transparent !important;
      --el-table-header-bg-color: transparent !important;
      --el-table-header-text-color: #feffff;
      --el-table-border-color: transparent !important;
      --el-table-text-color: #feffff;
      --el-table-row-hover-bg-color: #2abd2a73;
      font-size: 19px;
    }

    .el-button.is-link {
      color: #ffffff;
    }

    .el-button {
      font-size: 19px;
    }

    .el-table__empty-block {
      color: #ffffff;
    }
  }
}
.header {
  .el-menu {
    background-color: transparent !important;
    border-bottom: transparent !important;
    --el-menu-text-color: #fff !important;
  }
  .flex-grow {
    flex-grow: 1;
  }
}
</style>
