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
          <el-menu
            default-active="2"
            class="el-menu-vertical-demo"
            :collapse="isCollapse"
            @open="handleOpen"
            @close="handleClose"
          >
            <el-menu-item index="1">
              <el-icon v-show="isCollapse === true"><ArrowRightBold /></el-icon>
              <el-icon v-show="isCollapse === false"><ArrowLeftBold /></el-icon>
            </el-menu-item>
            <el-sub-menu index="2">
              <template #title>
                <el-icon><location /></el-icon>
                <span>Navigator One</span>
              </template>
              <el-menu-item-group>
                <template #title><span>Group One</span></template>
                <el-menu-item index="1-1">item one</el-menu-item>
                <el-menu-item index="1-2">item two</el-menu-item>
              </el-menu-item-group>
              <el-menu-item-group title="Group Two">
                <el-menu-item index="1-3">item three</el-menu-item>
              </el-menu-item-group>
              <el-sub-menu index="1-4">
                <template #title><span>item four</span></template>
                <el-menu-item index="1-4-1">item one</el-menu-item>
              </el-sub-menu>
            </el-sub-menu>
            <el-menu-item index="3">
              <el-icon><icon-menu /></el-icon>
              <template #title>Navigator Two</template>
            </el-menu-item>
            <el-menu-item index="4" disabled>
              <el-icon><document /></el-icon>
              <template #title>Navigator Three</template>
            </el-menu-item>
            <el-menu-item index="5">
              <el-icon><setting /></el-icon>
              <template #title>Navigator Four</template>
            </el-menu-item>
          </el-menu>
        </el-scrollbar>
      </el-aside>
      <el-main>
        <el-scrollbar> </el-scrollbar>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref } from "vue";
import { Menu as IconMenu, Message, Setting } from "@element-plus/icons-vue";
import HeaderBar from "@/views/Home/HeaderBar.vue";
import utils from "@/utils/utils";

const screenHeight = ref(document.documentElement.clientHeight - 60);
window.onresize = () => {
  screenHeight.value = document.documentElement.clientHeight - 60;
};
const isCollapse = ref(true);
const handleOpen = (key, keyPath) => {
  console.log(key, keyPath);
};
const handleClose = (key, keyPath) => {
  utils.showMessage(200, key);
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

.layout-container-demo .el-aside {
}

.layout-container-demo .el-menu {
  border-right: none;
}

.layout-container-demo .el-main {
  padding: 0;
  background-color: #f6f8fa;
}

.layout-container-demo .toolbar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  right: 20px;
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
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
</style>
