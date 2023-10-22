<template>
  <div class="components-container">
    <p class="ff">欢迎使用,{{ userName }}</p>
    <el-row>
      <el-col :span="8" :offset="8">
        <pan-thumb :image="image + '?' + new Date().getTime()" />
        <image-cropper
          v-show="imagecropperShow"
          :key="imagecropperKey"
          :width="300"
          :height="300"
          url="https://httpbin.org/post"
          lang-type="en"
          @close="close"
          @crop-upload-success="cropSuccess"
        />
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="8" :offset="8">
        <el-button
          type="primary"
          icon="UploadFilled"
          @click="imagecropperShow = true"
        >
          更改头像
        </el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import ImageCropper from "@/components/ImageCropper";
import PanThumb from "@/components/PanThumb";
import pinia from "@/store/store";
import { useUserStore } from "@/store/user";
import { storeToRefs } from "pinia/dist/pinia";
const user = useUserStore(pinia);
const { image } = storeToRefs(user);
const { userName } = storeToRefs(user);
const buttonWeight = 108;
export default {
  name: "AvatarUploadDemo",
  components: { ImageCropper, PanThumb },
  data() {
    return {
      imagecropperShow: false,
      imagecropperKey: 0,
      image: image,
      userName: userName,
    };
  },
  methods: {
    cropSuccess(resData) {
      this.imagecropperShow = false;
      this.imagecropperKey = this.imagecropperKey + 1;
      this.image = resData.files.avatar;
    },
    close() {
      this.imagecropperShow = false;
    },
  },
};
</script>

<style scoped lang="scss">
.components-container {
  background-color: #ffffff;
  height: 200px;
  .el-button {
    margin-top: 10px;
  }
}
.ff {
  font-family: "Google Sans", Roboto, Arial, sans-serif;
  line-height: 2.25rem;
  font-size: 1.75rem;
  letter-spacing: 0;
  font-weight: 400;
  -webkit-hyphens: auto;
  hyphens: auto;
  word-break: break-word;
  word-wrap: break-word;
  color: var(--gm3-sys-color-on-background, #1f1f1f);
  text-align: center;
}
</style>
