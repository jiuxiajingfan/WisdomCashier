<template>
  <div class="userMessage" style="margin-bottom: 3%">
    <el-space wrap>
      <el-card style="width: 600px; height: 350px">
        <template #header>
          <div class="card-header">
            <span>用户信息</span>
            <el-switch v-show="false" v-model="userModel.change" />
          </div>
        </template>
        <el-form
          style="margin-top: 4%"
          :model="userModel"
          label-width="120px"
          label-position="right"
        >
          <el-form-item label="用户标识 :">
            {{ user.getId }}
            <el-tooltip
              class="box-item"
              effect="dark"
              content="点击复制"
              placement="top-start"
            >
              <el-button
                type="text"
                :icon="CopyDocument"
                style="margin-left: 5px"
                class="copyBtn"
                @click="copy"
                circle
              />
            </el-tooltip>
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
            <el-input type="text" v-model="userModel.email"></el-input>
          </el-form-item>
          <el-form-item label="验证码 :">
            <el-input type="text" v-model="userModel.code">
              <template #append>
                <el-button @click="getEmailCode">发送</el-button>
              </template>
            </el-input>
          </el-form-item>
          <el-button @click="changeEmail">确定</el-button>
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
          :rules="rules"
          ref="changePasswordModel"
        >
          <el-form-item label="原密码 :" prop="pwdOriginal">
            <el-input
              type="password"
              v-model="userModel.pwdOriginal"
              show-password
            ></el-input>
          </el-form-item>
          <el-form-item label="新密码 :" prop="pwdNew">
            <el-input
              type="password"
              v-model="userModel.pwdNew"
              show-password
            ></el-input>
          </el-form-item>
          <el-form-item label="确认密码 :" prop="pwdNew2">
            <el-input
              type="password"
              v-model="userModel.pwdNew2"
              show-password
            ></el-input>
          </el-form-item>
          <el-button @click="changePassword">确定</el-button>
        </el-form>
      </el-card>
    </el-space>
  </div>
</template>

<script setup>
import md5 from "js-md5";
import utils from "@/utils/utils";
import ClipboardJS from "clipboard";
import { reactive, ref } from "vue";
import { useUserStore } from "@/store/user";
import pinia from "@/store/store";
import api from "@/api/api";
const user = useUserStore(pinia);
const passwordButton = ref(false);
const emailButton = ref(false);

const userModel = reactive({
  id: user.getId,
  name: "",
  phone: user.getPhone,
  email: "",
  change: false,
  code: "",
  pwdOriginal: "",
  pwdNew: "",
  pwdNew2: "",
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
const getEmailCode = () => {
  api
    .get("/account/getcodeAuth", {
      // get请求使用params传参,并且最后会拼接到url后面
      params: {
        type: 1,
      },
    })
    .then((res) => {
      utils.showMessage(
        res.data.code,
        res.data.code == 0 ? res.data.data : res.data.msg
      );
    });
};
const changeEmail = () => {
  api
    .post("/changeUserEmail", {
      email: userModel.email,
      code: userModel.code,
    })
    .then((res) => {
      utils.showMessage(res.data.code, res.data.msg);
    });
};
const checkPassword = (rule, value, callback) => {
  //验证密码的正则表达式
  const regEmail = /^(?![a-zA-Z]+$)(?!\d+$)(?![^\da-zA-Z\s]+$).{8,30}$/;
  if (regEmail.test(value)) {
    return callback();
  }
  callback(
    new Error("密码必须包含数字、字母、特殊符号中的两种，长度为8-30位！")
  );
};
const rules = reactive({
  pwdOriginal: [
    { required: true, message: "请输入密码", trigger: "blur" },
    {
      validator: checkPassword,
      min: 8,
      max: 30,
      message: "密码必须包含数字、字母、特殊符号中的两种，长度为8-30位！",
      trigger: "blur",
    },
  ],
  pwdNew: [
    { required: true, message: "请输入密码", trigger: "blur" },
    {
      validator: checkPassword,
      min: 8,
      max: 30,
      message: "密码必须包含数字、字母、特殊符号中的两种，长度为8-30位！",
      trigger: "blur",
    },
  ],
  pwdNew2: [
    { required: true, message: "请输入密码", trigger: "blur" },
    {
      validator: checkPassword,
      min: 8,
      max: 30,
      message: "密码必须包含数字、字母、特殊符号中的两种，长度为8-30位！",
      trigger: "blur",
    },
  ],
  name: [
    { required: true, message: "请输入新用户名", trigger: "blur" },
    {
      min: 1,
      max: 0,
      message: "请输入新用户名，长度为1-20位！",
      trigger: "blur",
    },
  ],
});
const changePasswordModel = ref();
const changePassword = () => {
  // 数据校验
  changePasswordModel.value.validate((valid) => {
    if (valid) {
      api
        .post("/account/changePwd", {
          pwdOriginal: md5(userModel.pwdOriginal + user.getName),
          pwdNew: md5(userModel.pwdNew + user.getName),
          pwdConfirm: md5(userModel.pwdNew2 + user.getName),
        })
        .then((res) => {
          utils.showMessage(res.data.code, res.data.msg);
        });
    }
  });
};
</script>

<style scoped></style>
