<template>
  <div class="mm">
    <el-row>
      <el-col>
        <div>
          <avatarUpload></avatarUpload>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import avatarUpload from "@/components/userCenter/avatar-upload";
</script>

<style scoped lang="scss">
.mm {
  background-color: #ffffff;
  height: calc(100% - 10px);
}
</style>
