<template>
  <div class="login-page">
    <div class="box">
      <el-row align="middle">
        <el-col :span="5" :push="16">
          <h1>登录</h1>
          <el-form
            ref="ruleFormRef"
            :rules="loginRules"
            :model="registerFormData"
          >
            <el-form-item prop="name">
              <el-input
                size="large"
                prefix-icon="User"
                placeholder="请输入账号"
                v-model="registerFormData.name"
              ></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input
                size="large"
                prefix-icon="Lock"
                type="password"
                placeholder="请输入密码"
                v-model="registerFormData.password"
              ></el-input>
            </el-form-item>
            <el-button type="primary" @click="login">登录</el-button>
          </el-form>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import api from "@/api/api";
import md5 from "js-md5";
import { useAuthStore } from "@/store/auth";
import pinia from "@/store/store";
import utils from "@/utils/utils";
import router from "@/router";
let registerFormData = reactive({
  name: "",
  password: "",
});
const ruleFormRef = ref();
const REGEXP_PWD =
  /^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)]|[()])+$)(?!^.*[\u4E00-\u9FA5].*$)([^(0-9a-zA-Z)]|[()]|[a-z]|[A-Z]|[0-9]){8,18}$/;
const loginRules = reactive({
  // password: [
  //   {
  //     validator: (rule, value, callback) => {
  //       if (value === "") {
  //         callback(new Error("请输入密码"));
  //       } else if (!REGEXP_PWD.test(value)) {
  //         callback(
  //           new Error("密码格式应为8-18位数字、字母、符号的任意两种组合")
  //         );
  //       } else {
  //         callback();
  //       }
  //     },
  //     trigger: "blur",
  //   },
  // ],
  password: [
    {
      required: true,
      message: "请输入密码",
      trigger: "blur",
    },
  ],
  name: [
    { required: true, message: "请输入账号", trigger: "blur" },
    {
      min: 5,
      max: 20,
      message: "登录账号长度必须在5-20位之间！",
      trigger: "blur",
    },
  ],
});
const Auth = useAuthStore(pinia);
const login = () => {
  ruleFormRef.value.validate((valid) => {
    if (valid) {
      console.log(1);
      api
        .post("account/login", {
          userName: registerFormData.name,
          userPwd: md5(registerFormData.password + registerFormData.name),
          verification: "params.captchaVerification",
        })
        .then((res) => {
          if (res.data.code === 200) {
            Auth.setToken(res.data.msg);
            utils.showMessage(200, "登录成功，欢迎回来！");
            router.push("/userCenter");
          } else {
            utils.showErrMessage(res.data.msg);
          }
        });
    }
  });
};
</script>

<style scoped lang="scss">
.login-page {
  background: url("../../../public/img/12.jpg") no-repeat;
  /*background-position: center;*/
  height: 100%;
  width: 100%;
  background-size: cover;
  position: fixed;
}
.box {
  margin-top: calc(30vh);
}
</style>
