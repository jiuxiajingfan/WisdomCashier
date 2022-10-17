<template>
  <login></login>
</template>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
<script>
import LoginBox from "@/views/LoginBox";
import Login from "@/views/Login";
export default {
  components: { Login },
};
</script>
