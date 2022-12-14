<template>
  <div class="mm">
    <avatarUpload></avatarUpload>
    <br />
    <br />
    <br />
    <div class="userMessage">
      <el-space wrap>
        <el-card style="width: 600px; height: auto">
          <template #header>
            <div class="card-header">
              <span>用户信息</span>
              <el-switch v-show="false" v-model="userModel.change" />
            </div>
          </template>
          <el-form :model="userModel" label-width="120px">
            <el-form-item label="用户标识 :">
              {{ user.getId }}
              <el-icon style="margin-left: 5px"><CopyDocument /></el-icon>
            </el-form-item>
            <el-form-item label="用户名 :">
              {{ user.getNickName }}
            </el-form-item>
            <el-form-item label="手机号 :">
              {{ user.getPhone }}
            </el-form-item>
            <el-form-item label="邮箱 :">
              {{ user.getEmail }}
            </el-form-item>
          </el-form>
        </el-card>
      </el-space>
      <el-space direction="vertical">
        <el-card style="width: 600px; height: auto">
          <template #header>
            <div class="card-header">
              <span>信息修改</span>
              <el-switch v-model="userModel.change" />
            </div>
          </template>
          <el-form
            :model="userModel"
            label-width="120px"
            v-show="userModel.change"
          >
            <el-form-item label="用户名 :">
              <el-input
                type="text"
                :disabled="!userModel.change"
                v-model="userModel.name"
              ></el-input>
            </el-form-item>
            <el-button @click="changeName">确定</el-button>
          </el-form>
        </el-card>
        <el-card style="width: 600px; height: auto">
          <template #header>
            <div class="card-header">
              <span>邮箱修改</span>
              <el-switch v-model="emailButton" />
            </div>
          </template>
          <el-form :model="userModel" label-width="120px" v-show="emailButton">
            <el-form-item label="新邮箱 :">
              <el-input type="text" v-model="userModel.phone"></el-input>
            </el-form-item>
            <el-form-item label="验证码 :">
              <el-input type="text" v-model="userModel.name"></el-input>
              <el-button>发送</el-button>
            </el-form-item>
            <el-button>确定</el-button>
          </el-form>
        </el-card>
        <el-card style="width: 600px; height: auto">
          <template #header>
            <div class="card-header">
              <span>密码修改</span>
              <el-switch v-model="passwordButton" />
            </div>
          </template>
          <el-form
            :model="userModel"
            label-width="120px"
            v-show="passwordButton"
          >
            <el-form-item label="原密码 :">
              <el-input type="password" v-model="userModel.name"></el-input>
            </el-form-item>
            <el-form-item label="新密码 :">
              <el-input type="password" v-model="userModel.phone"></el-input>
            </el-form-item>
            <el-form-item label="确认密码 :">
              <el-input type="password" v-model="userModel.email"></el-input>
            </el-form-item>
            <el-button>确定</el-button>
          </el-form>
        </el-card>
      </el-space>
    </div>
  </div>
</template>

<script setup>
import { useUserStore } from "@/store/user";
import pinia from "@/store/store";
import avatarUpload from "@/components/userCenter/avatar-upload";
import { onMounted, reactive, ref } from "vue";
import api from "@/api/api";
import utils from "@/utils/utils";
const user = useUserStore(pinia);
const userModel = reactive({
  id: user.getId,
  name: "",
  phone: user.getPhone,
  email: user.getEmail,
  change: false,
});
const passwordButton = ref(false);
const emailButton = ref(false);
const form = reactive({
  name: "",
  region: "",
  date1: "",
  date2: "",
  delivery: false,
  type: [],
  resource: "",
  desc: "",
});
const changeName = () => {
  api
    .get("/user/changeUserNickName", {
      params: {
        name: userModel.name,
      },
    })
    .then((res) => {
      utils.showMessage(res.data.code, res.data.msg);
      user.setNickName(userModel.name);
      userModel.change = false;
      userModel.name = "";
    });
};
const onSubmit = () => {
  console.log("submit!");
};
</script>

<style scoped lang="scss">
.mm {
  background-color: #ffffff;
  min-height: v-bind(screenHeight);
}
</style>
