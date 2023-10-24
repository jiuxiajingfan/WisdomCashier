<template>
  <div class="header">
    <el-avatar :src="image + '?' + new Date().getTime()" />
    <el-dropdown @command="handleCommand">
      <el-dropdown-link>
        <div class="name">
          <span>{{ userNickName }}</span>
          <el-icon class="el-icon--right">
            <CaretBottom />
          </el-icon>
        </div>
      </el-dropdown-link>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item command="a">
            <el-icon>
              <User />
            </el-icon>
            <span>个人中心</span>
          </el-dropdown-item>
          <el-dropdown-item command="b">
            <el-icon>
              <SwitchButton />
            </el-icon>
            <span>退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script setup>
import { useUserStore } from "@/store/user";
import pinia from "@/store/store";
import router from "@/router";
import api from "@/api/api";
import utils from "@/utils/utils";
import { useAuthStore } from "@/store/auth";
import { ElMessageBox } from "element-plus";
import { storeToRefs } from "pinia/dist/pinia";

const user = useUserStore(pinia);
const store = useAuthStore(pinia);
const { image, userNickName } = storeToRefs(user);
const handleCommand = (command) => {
  if (command === "a") {
    router.push("/home");
  } else if (command === "b") {
    ElMessageBox.confirm("您确定要退出登录吗？", "", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }).then(() => {
      api.post("/account/loginOut").then(() => {
        utils.showMessage(200, "账户退出成功！");
        store.setToken("null");
        router.push("/login");
      });
    });
  }
};
</script>

<style scoped lang="scss">
.header {
  .el-icon {
    color: #ffffff;
  }
  .el-avatar {
    margin-right: 10px;
  }
}

.name {
  text-align: center;
  margin-top: 13px;
  font-size: revert;
  color: #ffffff;
}
</style>
