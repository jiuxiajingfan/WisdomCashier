<template>
  <div class="header">
    <div class="header">
      <el-dropdown @command="handleCommand">
        <span class="el-dropdown-link">
          <el-avatar :src="imagePath" />
          <el-icon class="el-icon--right"><CaretBottom /></el-icon>
        </span>
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

const user = useUserStore(pinia);
const store = useAuthStore(pinia);
const imagePath = user.getImage;
const handleCommand = (command) => {
  if (command === "a") {
    router.push("/userCenter");
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
  .photo {
    .el-avatar {
      margin-top: 10px;
    }
  }
  .el-icon {
    color: #ffffff;
  }
}
</style>
