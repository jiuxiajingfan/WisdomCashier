<template>
  <div class="components-container">
    <pan-thumb :image="image" />
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
    <el-button
      type="primary"
      icon="UploadFilled"
      @click="imagecropperShow = true"
    >
      更改头像
    </el-button>
  </div>
</template>

<script>
import ImageCropper from "@/components/ImageCropper";
import PanThumb from "@/components/PanThumb";
import pinia from "@/store/store";
import { useUserStore } from "@/store/user";
const user = useUserStore(pinia);
const buttonWeight = 108;
export default {
  name: "AvatarUploadDemo",
  components: { ImageCropper, PanThumb },
  data() {
    return {
      imagecropperShow: false,
      imagecropperKey: 0,
      image: user.getImage,
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
    position: relative;
    left: -129px;
    margin-top: 67px;
  }
}
</style>
