<template>
  <el-menu class="el-menu-vertical-demo" :collapse="isCollapse">
    <div class="receive">
      <el-button @click="contraction" type="text" style="width: 100%">
        <el-icon v-if="isCollapse === true">
          <ArrowRightBold />
        </el-icon>
        <el-icon v-if="isCollapse === false">
          <ArrowLeftBold />
        </el-icon>
      </el-button>
    </div>
    <el-sub-menu index="">
      <template #title>
        <el-icon><Avatar /></el-icon>
        <span>账号设置</span>
      </template>
      <el-menu-item index="2-1">
        <el-icon><User /></el-icon>
        我的信息
      </el-menu-item>
      <el-menu-item index="2-2">
        <el-icon><Key /></el-icon>
        密码修改
      </el-menu-item>
    </el-sub-menu>
    <el-menu-item index="3">
      <el-icon>
        <icon-menu />
      </el-icon>
      <template #title>Navigator Two</template>
    </el-menu-item>
    <el-menu-item index="5">
      <el-icon>
        <setting />
      </el-icon>
      <template #title>Navigator Four</template>
    </el-menu-item>
  </el-menu>
</template>

<script setup>
import {
  Document,
  Menu as IconMenu,
  Location,
  Setting,
} from "@element-plus/icons-vue";
import { ref } from "vue";
const isCollapse = ref(false);
const buttonWidth = ref("200px");
const contraction = () => {
  isCollapse.value = !isCollapse.value;
  if (isCollapse.value) {
    buttonWidth.value = "62px";
  } else {
    buttonWidth.value = "200px";
  }
};
</script>

<style scoped lang="scss">
.layout-container-demo .el-menu {
  border-right: none;
}
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
.receive {
  .el-button {
    height: 56px;
    min-width: 62px;
  }
  .el-button--text {
    color: #606266;
  }
}
</style>
