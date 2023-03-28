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

let registerFormData = reactive({
  name: "",
  password: "",
});
const ruleFormRef = ref();
const REGEXP_PWD =
  /^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)]|[()])+$)(?!^.*[\u4E00-\u9FA5].*$)([^(0-9a-zA-Z)]|[()]|[a-z]|[A-Z]|[0-9]){8,18}$/;
const loginRules = reactive({
  password: [
    {
      validator: (rule, value, callback) => {
        if (value === "") {
          callback(new Error("请输入密码"));
        } else if (!REGEXP_PWD.test(value)) {
          callback(
            new Error("密码格式应为8-18位数字、字母、符号的任意两种组合")
          );
        } else {
          callback();
        }
      },
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
const login = () => {
  ruleFormRef.value.validate((valid) => {
    console.log(1);
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
