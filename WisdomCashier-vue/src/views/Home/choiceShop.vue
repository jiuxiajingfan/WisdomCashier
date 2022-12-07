<template>
  <div class="back">
    <div class="header">
      <el-header style="text-align: right; font-size: 12px">
        <el-avatar :src="imagePath" />
        <el-dropdown @command="handleCommand">
          <span class="el-dropdown-link">
            <div class="photo">
              <span> {{ userNickName }}</span>
              <el-icon class="el-icon--right"><CaretBottom /></el-icon>
            </div>
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
      </el-header>
    </div>
    <div class="title">
      <h1>请选择店铺</h1>
    </div>
    <div class="box">
      <dev class="searchBox">
        <el-input
          placeholder="请输入要搜索的店铺名"
          v-model="searchText"
          class="input-with-select"
          @keyup.enter="searchShop"
        >
          <template #append>
            <el-button icon="Search" @click="searchShop" />
          </template>
        </el-input>
      </dev>
      <br />
      <br />
      <br />
      <div class="table">
        <el-table
          :data="shops"
          style="width: 100%"
          :show-header="false"
          :height="tableheight"
        >
          <el-table-column label="Operations" align="center">
            <template v-slot="scope">
              <el-button link type="primary" size="large">
                {{ scope.row.shopName }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <br />
      <br />
      <div class="foot"></div>
    </div>
  </div>
</template>

<script setup>
import { onBeforeMount, onMounted } from "vue";
import { ref } from "vue";
import api from "@/api/api";
import utils from "@/utils/utils";
import router from "@/router";
import { useAuthStore } from "@/store/auth";
import pinia from "@/store/store";
import { useUserStore } from "@/store/user";
import { ElMessage, ElMessageBox } from "element-plus";

const store = useAuthStore(pinia);
const user = useUserStore(pinia);
const shops = ref([]);
const tableheight = ref(document.documentElement.clientHeight * 0.32);
const searchText = ref("");
const imagePath = ref("null");
const userNickName = ref("");
onBeforeMount(() => {
  api.get("choiceShop/getUserShop").then((res) => {
    shops.value = res.data.data;
  });
  api.post("/user/getUser").then((res) => {
    let data = res.data.data;
    user.setId(data.id);
    user.setImage(data.image);
    user.setName(data.userName);
    user.setNickName(data.userNickname);
    imagePath.value = data.image;
    userNickName.value = data.userNickname;
  });
});
const searchShop = () => {
  console.log(searchText.value);
  api
    .get("choiceShop/getUserShop", {
      params: {
        shopName: searchText.value,
      },
    })
    .then((res) => {
      shops.value = res.data.data;
    });
};
window.onresize = () => {
  tableheight.value = document.documentElement.clientHeight * 0.32;
  console.log(tableheight.value);
};
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

    .el-table__empty-block {
      color: #ffffff;
    }
  }
}

.header {
  .el-icon {
    color: #ffffff;
  }
  .el-avatar {
    margin-right: 10px;
    margin-top: 10px;
  }
  .el-header {
    background-color: transparent !important;
    height: 60px !important;
  }
  .photo {
    text-align: center;
    margin-top: 22px;
    font-size: revert;
    color: #ffffff;
  }
}
</style>
