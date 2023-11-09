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
            :default-active="openeds"
            router
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
                v-if="item.hasOwnProperty('children') && item.hidden == 0"
                :index="index"
                :disabled="item.status == 0"
              >
                <template v-slot:title>
                  <el-icon>
                    <component :is="item.icon"></component>
                  </el-icon>
                  <span>{{ item.name }}</span>
                </template>
                <template v-if="item.parentId === null">
                  <template
                    v-for="(item2, index2) in item.children"
                    :key="index2"
                  >
                    <el-menu-item
                      :index="item2.component"
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
              <el-menu-item
                v-if="!item.hasOwnProperty('children') && item.hidden == 0"
                :index="item.component"
                :disabled="item.status == 0"
              >
                <template v-slot:title>
                  <el-icon>
                    <component :is="item.icon"></component>
                  </el-icon>
                  <span>{{ item.name }}</span>
                </template>
              </el-menu-item>
            </template>
          </el-menu>
        </el-scrollbar>
      </el-aside>
      <el-main>
        <el-scrollbar>
          <router-view></router-view>
        </el-scrollbar>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { onBeforeMount, ref } from "vue";
import { Menu as IconMenu, Message, Setting } from "@element-plus/icons-vue";
import HeaderBar from "@/views/Home/HeaderBar.vue";
import api from "@/api/api";
import router from "@/router";
import utils from "@/utils/utils";
const menuData = ref([]);
const menuData2 = [];
const openeds = ref(router.currentRoute.value.name);
onBeforeMount(() => {
  api
    .get("shop/getShopMenu", {
      params: {
        shopId: router.currentRoute.value.query.id,
      },
    })
    .then((res) => {
      if (res.data.code !== 200) {
        utils.showErrMessage(res.data.msg);
        router.go("/myShop");
      }
      menuData.value = res.data.data;
      menuData2.push(res.data.data);
    })
    .catch(() => {
      router.go(-1);
    });
});
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
