<template>
  <div class="back">
    <div class="title">
      <h1>请选择店铺</h1>
    </div>
    <div class="box">
      <div class="table">
        <el-table
          :data="tableData"
          style="width: 100%"
          height="250"
          :show-header="false"
        >
          <el-table-column prop="date" label="Date" align="center" />
          <el-table-column label="Operations" align="center">
            <template #default>
              <el-button link type="primary" size="huge" @click="handleClick">
                进入
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script setup>
import zhCn from "element-plus/dist/locale/zh-cn.mjs";

const tableData = [
  {
    date: "2016-05-03",
    name: "Tom",
    state: "California",
    city: "Los Angeles",
    address: "No. 189, Grove St, Los Angeles",
    zip: "CA 90036",
    tag: "Home",
  },
  {
    date: "2016-05-032016-05-032016-05-032016-05-032016-05-032016-05-032016-05-032016-05-032016-05-032016-05-032016-05-032016-05-03",
    name: "Tom",
    state: "California",
    city: "Los Angeles",
    address: "No. 189, Grove St, Los Angeles",
    zip: "CA 90036",
    tag: "Home",
  },
  {
    date: "2016-05-03",
    name: "Tom",
    state: "California",
    city: "Los Angeles",
    address: "No. 189, Grove St, Los Angeles",
    zip: "CA 90036",
    tag: "Home",
  },
  {
    date: "2016-05-03",
    name: "Tom",
    state: "California",
    city: "Los Angeles",
    address: "No. 189, Grove St, Los Angeles",
    zip: "CA 90036",
    tag: "Home",
  },
  {
    date: "2016-05-03",
    name: "Tom",
    state: "California",
    city: "Los Angeles",
    address: "No. 189, Grove St, Los Angeles",
    zip: "CA 90036",
    tag: "Home",
  },
  {
    date: "2016-05-03",
    name: "Tom",
    state: "California",
    city: "Los Angeles",
    address: "No. 189, Grove St, Los Angeles",
    zip: "CA 90036",
    tag: "Home",
  },
  {
    date: "2016-05-03",
    name: "Tom",
    state: "California",
    city: "Los Angeles",
    address: "No. 189, Grove St, Los Angeles",
    zip: "CA 90036",
    tag: "Home",
  },
  {
    date: "2016-05-03",
    name: "Tom",
    state: "California",
    city: "Los Angeles",
    address: "No. 189, Grove St, Los Angeles",
    zip: "CA 90036",
    tag: "Home",
  },
  {
    date: "2016-05-03",
    name: "Tom",
    state: "California",
    city: "Los Angeles",
    address: "No. 189, Grove St, Los Angeles",
    zip: "CA 90036",
    tag: "Home",
  },
  {
    date: "2016-05-02",
    name: "Tom",
    state: "California",
    city: "Los Angeles",
    address: "No. 189, Grove St, Los Angeles",
    zip: "CA 90036",
    tag: "Office",
  },
  {
    date: "2016-05-04",
    name: "Tom",
    state: "California",
    city: "Los Angeles",
    address: "No. 189, Grove St, Los Angeles",
    zip: "CA 90036",
    tag: "Home",
  },
  {
    date: "2016-05-01",
    name: "Tom",
    state: "California",
    city: "Los Angeles",
    address: "No. 189, Grove St, Los Angeles",
    zip: "CA 90036",
    tag: "Office",
  },
];
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
    }
    .el-button.is-link {
      color: #ffffff;
    }
  }
}
</style>
