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
              <el-menu-item index="2-1" @click="addTab(editableTabsValue)">
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
        </el-scrollbar>
      </el-aside>
      <el-main>
        <el-scrollbar>
          <el-tabs
            v-model="editableTabsValue"
            type="card"
            class="demo-tabs"
            closable
            @tab-remove="removeTab"
          >
            <el-tab-pane
              v-for="item in editableTabs"
              :key="item.name"
              :label="item.title"
              :name="item.name"
            >
            </el-tab-pane>
            <component v-bind:is="myMessage"></component>
          </el-tabs>
        </el-scrollbar>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref } from "vue";
import { Menu as IconMenu, Message, Setting } from "@element-plus/icons-vue";
import HeaderBar from "@/views/Home/HeaderBar.vue";
import myMessage from "../../components/userCenter/myMessage.vue";
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
const screenHeight = ref(document.documentElement.clientHeight - 60);
window.onresize = () => {
  screenHeight.value = document.documentElement.clientHeight - 60;
};
let tabIndex = 2;
const editableTabsValue = ref("2");
const editableTabs = ref([
  {
    title: "Tab 1",
    name: "1",
    content: "Tab 1 content",
  },
  {
    title: "Tab 2",
    name: "2",
    content: "Tab 2 content",
  },
]);

const addTab = (targetName) => {
  const newTabName = `${++tabIndex}`;
  editableTabs.value.push({
    title: "New Tab",
    name: newTabName,
    content: "New Tab content",
  });
  editableTabsValue.value = newTabName;
};
const removeTab = (targetName) => {
  const tabs = editableTabs.value;
  let activeName = editableTabsValue.value;
  if (activeName === targetName) {
    tabs.forEach((tab, index) => {
      if (tab.name === targetName) {
        const nextTab = tabs[index + 1] || tabs[index - 1];
        if (nextTab) {
          activeName = nextTab.name;
        }
      }
    });
  }

  editableTabsValue.value = activeName;
  editableTabs.value = tabs.filter((tab) => tab.name !== targetName);
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
