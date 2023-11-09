<template>
  <div class="userMessage">
    <el-card style="margin-top: 20vh">
      <el-collapse v-model="activeName" accordion>
        <el-collapse-item name="1">
          <template #title>
            <el-icon size="25" color="#1e9fff"><Postcard /></el-icon>
            <span class="title">信息修改</span>
          </template>
          <el-form :model="userModel" label-width="120px">
            <el-form-item label="用户名 :">
              <el-input
                :placeholder="userModel.name"
                type="text"
                v-model="userModel.name"
              ></el-input>
            </el-form-item>
            <el-button @click="changeName">确定</el-button>
          </el-form>
        </el-collapse-item>
        <el-collapse-item name="2">
          <template #title>
            <el-icon size="25" color="#1e9fff"><Message /></el-icon>
            <span class="title">邮箱修改</span>
          </template>
          <el-form :model="userModel" label-width="120px">
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
        </el-collapse-item>
        <el-collapse-item name="3">
          <template #title>
            <div class="card-header">
              <el-icon size="25" color="#1e9fff"><Lock /></el-icon>
              <span class="title">密码修改</span>
            </div>
          </template>
          <el-form
            :model="userModel"
            label-width="120px"
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
        </el-collapse-item>
      </el-collapse>
    </el-card>
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
const activeName = ref("1");
const userModel = reactive({
  id: user.getId,
  name: user.getNickName,
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
    .get("/account/changeUserNickName", {
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
    .get("/email/getCode", {
      // get请求使用params传参,并且最后会拼接到url后面
      params: {
        type: 1,
        email: "1",
      },
    })
    .then((res) => {
      utils.showMessage(
        res.data.code,
        res.data.code == 0 ? res.data.data : res.data.msgsh
      );
    });
};
const changeEmail = () => {
  api
    .post("/account/changeUserEmail", {
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

<style scoped>
.userMessage {
  width: 60vw;
  margin: 0 auto;
}
.title {
  font-size: 25px;
  color: #1e9fff;
}
</style>
