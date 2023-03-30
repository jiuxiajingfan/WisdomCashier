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
        <el-scrollbar>
          <el-menu
            class="el-menu-vertical-demo"
            :collapse="isCollapse"
            :default-openeds="openeds"
          >
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
            <template v-for="(item, index) in menuData" :key="index">
              <el-sub-menu
                v-if="item.hidden == 0"
                :index="index"
                :disabled="item.status == 0"
              >
                <template v-slot:title>
                  <el-icon>
                    <component :is="item.icon"></component>
                  </el-icon>
                  <span>{{ item.name }}</span>
                </template>
                <template v-if="item.children.length > 0">
                  <template
                    v-for="(item2, index2) in item.children"
                    :key="index2"
                  >
                    <el-menu-item
                      :index="item2.component"
                      @click="
                        addTab(editableTabsValue, item2.component, item2.name)
                      "
                      :disabled="item2.status == 0"
                    >
                      <el-icon>
                        <component :is="item2.icon"></component>
                      </el-icon>
                      <span>{{ item2.name }}</span>
                    </el-menu-item>
                  </template>
                </template>
              </el-sub-menu>
            </template>
          </el-menu>
        </el-scrollbar>
      </el-aside>
      <el-main>
        <el-scrollbar>
          <el-tabs
            v-model="focus"
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
              <component :is="map.get(item.content)"></component>
            </el-tab-pane>
            <component v-if="cnt == 0" :is="SystemStatus"></component>
          </el-tabs>
        </el-scrollbar>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { defineAsyncComponent, onBeforeMount, ref } from "vue";
import HeaderBar from "@/components/HeaderBar";
import pinia from "@/store/store";
import { useUserStore } from "@/store/user";
import api from "@/api/api";
const cnt = ref(0);
const menuData = ref([]);
const map = new Map();
const menuData2 = [];
const openeds = [0];
const isCollapse = ref(false);
const buttonWidth = ref("200px");
const User = useUserStore(pinia);
const contraction = () => {
  isCollapse.value = !isCollapse.value;
  if (isCollapse.value) {
    buttonWidth.value = "62px";
  } else {
    buttonWidth.value = "200px";
  }
};
const editableTabs = ref([
  {
    title: "系统信息",
    name: "SystemStatus",
    content: "SystemStatus",
  },
]);
let SystemStatus = defineAsyncComponent(() =>
  import("../../components/system/SystemStatus")
);
let UserList = defineAsyncComponent(() =>
  import("../../components/user/UserList")
);
let ShopLost = defineAsyncComponent(() =>
  import("../../components/shop/ShopList")
);
let ShopApply = defineAsyncComponent(() =>
  import("../../components/shop/ShopApply")
);
const focus = ref("SystemStatus");
const mapTab = new Map();
mapTab.set("SystemStatus", "SystemStatus");
const addTab = (targetName, component, title) => {
  if (mapTab.get(component) !== component) {
    cnt.value = cnt.value + 1;
    editableTabs.value.push({
      title: title,
      name: component,
      content: component,
    });
    mapTab.set(component, component);
    focus.value = component;
  } else {
    focus.value = component;
  }
};
const removeTab = (targetName) => {
  const tabs = editableTabs.value;
  let activeName = focus.value;
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
  mapTab.delete(targetName);
  focus.value = activeName;
  editableTabs.value = tabs.filter((tab) => tab.name !== targetName);
};
onBeforeMount(() => {
  api.post("/user/getUser").then((res) => {
    User.clear();
    User.add({
      userId: res.data.data.id,
      image: res.data.data.image,
      userName: res.data.data.userName,
      userNickName: res.data.data.userNickname,
      phone: res.data.data.phone,
      email: res.data.data.email,
    });
  });
  api.get("account/getAdminCenterMenu").then((res) => {
    menuData.value = res.data.data;
    menuData2.push(res.data.data);
    for (let i = 0; i < menuData2[0].length; i++) {
      if (menuData2[0][i].children.length > 0) {
        for (let j = 0; j < menuData2[0][i].children.length; j++) {
          map.set(
            menuData2[0][i].children[j].component,
            eval(menuData2[0][i].children[j].component)
          );
        }
      }
    }
  });
});
</script>

<style scoped lang="scss">
.layout-container-demo .el-header {
  position: relative;
  background-color: #24292f;
  height: 65px !important;
  //color: var(--el-text-color-primary);
  //display: flex;
  //justify-content: space-between;
}

.layout-container-demo .el-main {
  padding: 0;
  height: calc(100vh - 65px);
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

.cop {
  position: relative;
}
.el-aside {
  height: calc(100vh - 65px);
}
</style>
