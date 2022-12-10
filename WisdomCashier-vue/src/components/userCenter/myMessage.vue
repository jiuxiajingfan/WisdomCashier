<template>
  <div class="mm">
    <avatarUpload></avatarUpload>
    <br />
    <br />
    <br />
    <div class="userMessage">
      <el-form :model="userModel" label-width="120px">
        <el-form-item label="用户标识 :">
          {{ user.getId }}
          <el-icon style="margin-left: 5px"><CopyDocument /></el-icon>
        </el-form-item>
        <el-form-item label="用户名 :">
          <el-input
            type="text"
            :disabled="!userModel.change"
            v-model="userModel.name"
          ></el-input>
        </el-form-item>
        <el-form-item label="手机号 :">
          <el-input
            type="text"
            :disabled="!userModel.change"
            v-model="userModel.phone"
          ></el-input>
        </el-form-item>
        <el-form-item label="邮箱 :">
          <el-input
            type="text"
            :disabled="!userModel.change"
            v-model="userModel.email"
          ></el-input>
        </el-form-item>
        <el-form-item label="修改信息">
          <el-switch v-model="userModel.change" />
        </el-form-item>
      </el-form>
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
  height: v-bind(screenHeight);
}
</style>
