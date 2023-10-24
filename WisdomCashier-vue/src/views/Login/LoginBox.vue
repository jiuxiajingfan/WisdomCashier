<template>
  <div class="main-box">
    <div :class="['container', 'container-register', { 'is-txl': isLogin }]">
      <el-form ref="registerForm" :model="registerFormData" :rules="rules">
        <h2 class="title">注册</h2>
        <el-form-item prop="name">
          <input
            v-model="registerFormData.name"
            class="form__input"
            type="text"
            placeholder="账号"
            @keyup.enter="this.$refs['passwordInput'].focus()"
          />
        </el-form-item>
        <el-form-item prop="password">
          <input
            v-model="registerFormData.password"
            class="form__input"
            type="password"
            placeholder="密码"
            ref="passwordInput"
            @keyup.enter="this.$refs['emailInput'].focus()"
          />
        </el-form-item>
        <el-form-item prop="email">
          <input
            v-model="registerFormData.email"
            class="form__input"
            type="text"
            placeholder="邮箱"
            ref="emailInput"
            @keyup.enter="this.$refs['codeInput'].focus()"
            @keydown.enter="getCode"
          />
        </el-form-item>
        <el-row>
          <el-col :span="15">
            <el-form-item prop="code">
              <input
                v-model="registerFormData.code"
                class="form__code"
                type="text"
                placeholder="验证码"
                ref="codeInput"
                @keyup.enter="register"
              />
            </el-form-item>
          </el-col>
          <el-col :span="9">
            <el-button
              class="primary-btn2"
              @click="getCode"
              :disabled="isCodeIng"
              >{{ codeBtnText }}</el-button
            >
          </el-col>
        </el-row>
        <div class="primary-btn" @click="register">注册</div>
      </el-form>
    </div>
    <div
      :class="['container', 'container-login', { 'is-txl is-z200': isLogin }]"
    >
      <el-form ref="loginForm" :model="loginFormData" :rules="rules2">
        <h2 class="title">立即使用</h2>
        <!--        <span class="text">or use email for registration</span>-->
        <el-form-item prop="name">
          <input
            class="form__input"
            type="text"
            placeholder="账号"
            v-model="loginFormData.name"
            @keyup.enter="this.$refs['loginPWDInput'].focus()"
          />
        </el-form-item>
        <el-form-item prop="password">
          <input
            class="form__input"
            type="password"
            placeholder="密码"
            ref="loginPWDInput"
            v-model="loginFormData.password"
            @keyup.enter="useVerify"
          />
        </el-form-item>
        <div class="primary-btn" @click="useVerify">登录</div>
      </el-form>
    </div>
    <div :class="['switch', { login: isLogin }]">
      <div class="switch__circle"></div>
      <div class="switch__circle switch__circle_top"></div>
      <div class="switch__container">
        <h2>{{ isLogin ? "你好！ 朋友" : "欢迎使用智慧商户系统" }}</h2>
        <p>
          {{
            isLogin
              ? "还没有账号？点击下方立即注册"
              : "助力实体发展，提供各行业解决方案"
          }}
        </p>
        <div class="primary-btn" @click="isLogin = !isLogin">
          {{ isLogin ? "注册" : "登录" }}
        </div>
      </div>
    </div>
  </div>
  <Verify
    @success="success"
    mode="pop"
    captchaType="blockPuzzle"
    :imgSize="{ width: '330px', height: '155px' }"
    ref="verify"
  ></Verify>
</template>

<script setup>
import { onUnmounted, reactive, ref } from "vue";
import md5 from "js-md5";
let isLogin = ref(false);
// 是否正在获取验证码
let isCodeIng = ref(false);
// 定时器
let timer = null;
// 时间
let curTime = ref(90);
// 获取验证码提示
let codeBtnText = ref("获取验证码");
const registerForm = ref();
let registerFormData = reactive({
  name: "",
  email: "",
  password: "",
  code: "",
});
onUnmounted(() => {
  timer && clearInterval(timer);
});
const getCode = () => {
  if (!registerFormData.email) {
    utils.showMessage(1, "请输入邮箱！");
    return;
  }
  isCodeIng.value = true;
  timer && clearInterval(timer);
  curTime.value = 90;
  api
    .get("/email/getCode", {
      // get请求使用params传参,并且最后会拼接到url后面
      params: {
        email: registerFormData.email,
        type: 0,
      },
    })
    .then((res) => {
      utils.showMessage(
        res.data.code,
        res.data.code == 0 ? res.data.data : res.data.msg
      );
    });

  timer = setInterval(() => {
    codeBtnText.value = curTime.value + "后重新获取";
    curTime.value--;
    if (curTime.value <= 0) {
      isCodeIng.value = false;
      timer && clearInterval(timer);
      codeBtnText.value = "获取验证码";
    }
  }, 1000);
};
const checkEmail = (rule, value, callback) => {
  //验证邮箱的正则表达式
  const regEmail = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
  if (regEmail.test(value)) {
    return callback();
  }
  callback(new Error("请输入合法的邮箱"));
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
const checkCode = (rule, value, callback) => {
  //验证验证码的正则表达式
  const regEmail = /^[a-zA-Z0-9]{6}$/;
  if (regEmail.test(value)) {
    return callback();
  }
  callback(new Error("请输入正确的验证码"));
};
const rules = reactive({
  name: [
    { required: true, message: "请输入账号", trigger: "blur" },
    {
      min: 5,
      max: 20,
      message: "登录账号长度必须在5-20位之间！",
      trigger: "blur",
    },
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    {
      validator: checkPassword,
      min: 8,
      max: 30,
      message: "密码必须包含数字、字母、特殊符号中的两种，长度为8-30位！",
      trigger: "blur",
    },
  ],
  email: [
    { required: true, message: "请输入邮箱", trigger: "blur" },
    {
      validator: checkEmail,
      min: 9,
      max: 18,
      message: "邮箱格式错误",
      trigger: "blur",
    },
  ],
  code: [
    { required: true, message: "请输入验证码", trigger: "blur" },
    {
      validator: checkCode,
      min: 6,
      max: 6,
      message: "请输入正确的验证码",
      trigger: "blur",
    },
  ],
});
const register = () => {
  // 数据校验
  registerForm.value.validate((valid) => {
    if (valid) {
      api
        .post("/account/createUser", {
          code: registerFormData.code,
          email: registerFormData.email,
          userName: registerFormData.name,
          password: md5(registerFormData.password + registerFormData.name),
        })
        .then((res) => {
          utils.showMessage(
            res.data.code,
            res.data.code == 0 ? res.data.data : res.data.msg
          );
          if (res.data.code === 200) {
            let store = useAuthStore();
            store.setToken(res.data.data);
            setUser();
            router.push("/home");
            utils.showMessage(res.data.code, "登录成功，欢迎回来！");
          }
        });
    }
  });
};
const setUser = () => {
  api.get("/account/getUserDetail").then((res) => {
    const user = useUserStore(pinia);
    let data = res.data.data;
    user.setId(data.id);
    user.setImage(data.image);
    user.setName(data.userName);
    user.setNickName(data.userNickname);
    user.setPhone(data.phone);
    user.setEmail(data.email);
  });
};
</script>
<script>
import Verify from "@/components/verifition/Verify";
import api from "@/api/api";
import utils from "@/utils/utils";
import { useAuthStore } from "@/store/auth";
import router from "@/router";
import pinia from "@/store/store";
import { useUserStore } from "@/store/user";
import { storeToRefs } from "pinia/dist/pinia";
export default {
  name: "loginBox",
  components: { Verify },
  data() {
    return {
      loginFormData: {
        email: "",
        password: "",
        code: "",
      },
      rules2: {
        name: [
          {
            required: true,
            message: "请输入账号",
            trigger: "blur",
          },
        ],
        password: [
          {
            required: true,
            message: "请输入密码",
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    success(params) {
      // params 返回的二次验证参数, 和登录参数一起回传给登录接口，方便后台进行二次验证
      api
        .post("account/login", {
          username: this.loginFormData.name,
          password: md5(this.loginFormData.password + this.loginFormData.name),
          verify: params.captchaVerification,
        })
        .then((res) => {
          if (res.data.code !== 200) {
            utils.showMessage(res.data.code, res.data.msg);
          } else {
            let store = useAuthStore();
            store.setToken("bearer " + res.data.data.token);
            api.get("/account/getUserDetail").then((res) => {
              const user = useUserStore(pinia);
              let data = res.data.data;
              user.setId(data.id);
              user.setImage(data.image);
              user.setName(data.userName);
              user.setNickName(data.userNickname);
              user.setPhone(data.phone);
              user.setEmail(data.email);
            });
            router.push("/home");
            utils.showMessage(res.data.code, "登录成功，欢迎回来！");
          }
        });
    },
    useVerify() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.$refs.verify.show();
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.main-box {
  margin: 0 auto;
  position: relative;
  opacity: 0.9;
  width: 1000px;
  min-width: 1000px;
  min-height: 600px;
  height: 600px;
  padding: 25px;
  border-radius: 12px;
  overflow: hidden;
  .container {
    display: flex;
    justify-content: center;
    align-items: center;
    position: absolute;
    top: 0;
    width: 600px;
    height: 100%;
    padding: 25px;
    background-color: #ecf0f3;
    transition: all 1.25s;
    form {
      display: flex;
      justify-content: center;
      align-items: center;
      flex-direction: column;
      width: 100%;
      height: 100%;
      color: #a0a5a8;
      .title {
        font-size: 34px;
        font-weight: 700;
        line-height: 3;
        color: #181818;
      }
      .text {
        margin-top: 30px;
        margin-bottom: 12px;
      }
      .form__input {
        width: 350px;
        height: 40px;
        margin: 4px 0;
        padding-left: 25px;
        font-size: 13px;
        letter-spacing: 0.15px;
        border: none;
        outline: none;
        // font-family: 'Montserrat', sans-serif;
        background-color: #ecf0f3;
        transition: 0.25s ease;
        border-radius: 8px;
        box-shadow: inset 2px 2px 4px #d1d9e6, inset -2px -2px 4px #f9f9f9;
        &::placeholder {
          color: #a0a5a8;
        }
      }
    }
  }
  .container-register {
    z-index: 100;
    left: calc(100% - 600px);
  }
  .container-login {
    left: calc(100% - 600px);
    z-index: 0;
  }
  .is-txl {
    left: 0;
    transition: 1.25s;
    transform-origin: right;
  }
  .is-z200 {
    z-index: 200;
    transition: 1.25s;
  }
  .switch {
    display: flex;
    justify-content: center;
    align-items: center;
    position: absolute;
    top: 0;
    left: 0;
    height: 100%;
    width: 400px;
    padding: 50px;
    z-index: 200;
    transition: 1.25s;
    background-color: #ecf0f3;
    overflow: hidden;
    box-shadow: 4px 4px 10px #d1d9e6, -4px -4px 10px #f9f9f9;
    color: #a0a5a8;
    .switch__circle {
      position: absolute;
      width: 500px;
      height: 500px;
      border-radius: 50%;
      background-color: #ecf0f3;
      box-shadow: inset 8px 8px 12px #d1d9e6, inset -8px -8px 12px #f9f9f9;
      bottom: -60%;
      left: -60%;
      transition: 1.25s;
    }
    .switch__circle_top {
      top: -30%;
      left: 60%;
      width: 300px;
      height: 300px;
    }
    .switch__container {
      display: flex;
      justify-content: center;
      align-items: center;
      flex-direction: column;
      position: absolute;
      width: 400px;
      padding: 50px 55px;
      transition: 1.25s;
      h2 {
        font-size: 34px;
        font-weight: 700;
        line-height: 3;
        color: #181818;
      }
      p {
        font-size: 14px;
        letter-spacing: 0.25px;
        text-align: center;
        line-height: 1.6;
      }
    }
  }
  .login {
    left: calc(100% - 400px);
    .switch__circle {
      left: 0;
    }
  }
  .primary-btn {
    width: 180px;
    height: 50px;
    border-radius: 25px;
    margin-top: 50px;
    text-align: center;
    line-height: 50px;
    //font-size: 14px;
    letter-spacing: 2px;
    background-color: #4b70e2;
    color: #f9f9f9;
    cursor: pointer;
    box-shadow: 8px 8px 16px #d1d9e6, -8px -8px 16px #f9f9f9;
    &:hover {
      box-shadow: 4px 4px 6px 0 rgb(255 255 255 / 50%),
        -4px -4px 6px 0 rgb(116 125 136 / 50%),
        inset -4px -4px 6px 0 rgb(255 255 255 / 20%),
        inset 4px 4px 6px 0 rgb(0 0 0 / 40%);
    }
  }
  .form__code {
    width: 206px;
    height: 40px;
    margin: 4px 0;
    padding-left: 25px;
    font-size: 13px;
    letter-spacing: 0.15px;
    border: none;
    outline: none;
    // font-family: 'Montserrat', sans-serif;
    background-color: #ecf0f3;
    transition: 0.25s ease;
    border-radius: 8px;
    box-shadow: inset 2px 2px 4px #d1d9e6, inset -2px -2px 4px #f9f9f9;
    &::placeholder {
      color: #a0a5a8;
    }
  }
  .primary-btn2 {
    width: 141px;
    height: 40px;
    border-radius: 25px;
    margin-top: 4px;
    text-align: center;
    line-height: 40px;
    letter-spacing: 2px;
    background-color: #4b70e2;
    color: #f9f9f9;
    cursor: pointer;
    box-shadow: 8px 8px 16px #d1d9e6, -8px -8px 16px #f9f9f9;
    &:hover {
      box-shadow: 4px 4px 6px 0 rgb(255 255 255 / 50%),
        -4px -4px 6px 0 rgb(116 125 136 / 50%),
        inset -4px -4px 6px 0 rgb(255 255 255 / 20%),
        inset 4px 4px 6px 0 rgb(0 0 0 / 40%);
    }
  }
}
</style>
