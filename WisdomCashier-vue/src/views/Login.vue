<template>
  <div class="login-page">
    <div class="login-header"></div>
    <div class="login-box">
      <login-box></login-box>
    </div>
  </div>
</template>

<script>
import LoginBox from "@/views/LoginBox";
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Login",
  components: { LoginBox },
};
</script>
<style scoped>
.login-page {
  background: url("../../public/img/12.jpg") no-repeat;
  background-position: center;
  height: 100%;
  width: 100%;
  background-size: cover;
  position: fixed;
  margin: 0;
}
.login-box {
  display: table-cell;
  width: 100vw;
  height: 100vh;
  vertical-align: middle;
  text-align: center;
}
</style>
