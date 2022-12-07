<template>
  <el-container class="layout-container-demo" style="height: 500px">
    <el-header style="text-align: right; font-size: 12px">
      <div class="toolbar">
        <HeaderBar></HeaderBar>
      </div>
      <i class="el-icon-s-fold" style="font-size: 20px"></i>
    </el-header>
    <el-container>
      <el-aside width="auto">
        <el-scrollbar :height="screenHeight">
          <MenuBar></MenuBar>
        </el-scrollbar>
      </el-aside>
      <el-main>
        <el-scrollbar></el-scrollbar>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref } from "vue";
import { Menu as IconMenu, Message, Setting } from "@element-plus/icons-vue";
import HeaderBar from "@/views/Home/HeaderBar.vue";
import MenuBar from "@/views/Home/MenuBar";

const screenHeight = ref(document.documentElement.clientHeight - 60);
window.onresize = () => {
  screenHeight.value = document.documentElement.clientHeight - 60;
};
</script>

<style scoped lang="scss">
.layout-container-demo .el-header {
  position: relative;
  background-color: #24292f;
  height: 60px !important;
  //color: var(--el-text-color-primary);
  //display: flex;
  //justify-content: space-between;
}

.layout-container-demo .el-main {
  padding: 0;
  background-color: #f6f8fa;
}

.layout-container-demo .el-scrollbar ::-webkit-scrollbar {
  /* 设置竖向滚动条的宽度 */
  width: 5px;
  /* 设置横向滚动条的高度 */
  height: 5px;
}

.layout-container-demo .el-scrollbar ::-webkit-scrollbar-thumb {
  /*滚动条的背景色*/
  background-color: rgba(144, 147, 153, 0.3);
  border-radius: 35px;
  position: relative;
}
.layout-container-demo .toolbar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  right: 20px;
}
</style>
