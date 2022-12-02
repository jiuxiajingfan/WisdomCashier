<template>
  <div class="back">
    <div class="title">
      <h1>请选择店铺</h1>
    </div>
    <div class="box">
      <dev class="searchBox">
        <el-input
          placeholder="请输入内容"
          v-model="input"
          class="input-with-select"
        >
          <template #append>
            <el-button icon="Search" />
          </template>
        </el-input>
      </dev>
      <br />
      <br />
      <br />
      <div class="table">
        <el-table :data="shops" style="width: 100%" :show-header="false">
          <el-table-column label="Operations" align="center">
            <template v-slot="scope">
              <el-button link type="primary" size="large" @click="handleClick">
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
onMounted(() => {
  api
    .get("choiceShop/getUserShop")
    .then((res) => {
      let date = res.data.data;
      shops.value = date;
      console.log(date);
    })
    .catch();
});
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
  }
}
</style>
