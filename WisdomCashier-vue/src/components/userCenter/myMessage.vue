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
            <el-button>确定修改</el-button>
          </el-form>
        </el-card>
        <el-card style="width: 600px; height: auto">
          <template #header>
            <div class="card-header">
              <span>邮箱修改</span>
              <el-switch v-model="userModel.change" />
            </div>
          </template>
          <el-form
            :model="userModel"
            label-width="120px"
            v-show="userModel.change"
          >
            <el-form-item label="新邮箱 :">
              <el-input
                type="text"
                :disabled="!userModel.change"
                v-model="userModel.phone"
              ></el-input>
            </el-form-item>
            <el-form-item label="验证码 :">
              <el-input
                type="text"
                :disabled="!userModel.change"
                v-model="userModel.name"
              ></el-input>
              <el-button>发送</el-button>
            </el-form-item>
          </el-form>
        </el-card>
        <el-card style="width: 600px; height: auto">
          <template #header>
            <div class="card-header">
              <span>密码修改</span>
              <el-switch v-model="userModel.change" />
            </div>
          </template>
          <el-form
            :model="userModel"
            label-width="120px"
            v-show="userModel.change"
          >
            <el-form-item label="原密码 :">
              <el-input
                type="password"
                :disabled="!userModel.change"
                v-model="userModel.name"
              ></el-input>
            </el-form-item>
            <el-form-item label="新密码 :">
              <el-input
                type="password"
                :disabled="!userModel.change"
                v-model="userModel.phone"
              ></el-input>
            </el-form-item>
            <el-form-item label="确认密码 :">
              <el-input
                type="password"
                :disabled="!userModel.change"
                v-model="userModel.email"
              ></el-input>
            </el-form-item>
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
const user = useUserStore(pinia);
const screenHeight = document.documentElement.clientHeight - 101 + "px";
const userModel = reactive({
  id: user.getId,
  name: user.getNickName,
  phone: user.getPhone,
  email: user.getEmail,
  change: false,
});
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
