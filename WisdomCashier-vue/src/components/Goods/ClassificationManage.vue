<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <el-row>
          <el-col :span="3">
            <h2 style="font-size: 30px">分类管理</h2>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="3">
            <el-button
              icon="Plus"
              class="button"
              type="primary"
              @click="dialogVisible = true"
              >新增分类</el-button
            >
          </el-col>
        </el-row>
      </div>
    </template>
    <el-space wrap>
      <el-tag
        v-for="tag in dynamicTags"
        :key="tag"
        effect="dark"
        closable
        size="large"
        :disable-transitions="false"
        @close="handleClose(tag)"
      >
        {{ tag }}
      </el-tag>
    </el-space>
  </el-card>
  <el-dialog
    v-model="dialogVisible"
    title="添加分类"
    width="30%"
    @focus="this.$refs['zfbinput'].focus()"
  >
    <h2 style="font-size: 30px">请输入分类名称</h2>
    <el-input ref="zfbinput" v-model="inputmodel" @keyup.enter="add"></el-input>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="add"> 确定 </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { nextTick, ref } from "vue";
import { ElInput } from "element-plus";
const inputmodel = ref("");
const inputValue = ref("");
const dialogVisible = ref(false);
const add = () => {
  console.log("hello");
};
const dynamicTags = ref([
  "Tag 1",
  "Tag 2",
  "Tag 3",
  "Tag 2",
  "Tag 3",
  "Tag 2",
  "Tag 3",
  "Tag 2",
  "Tag 3",
  "Tag 2",
  "Tag 3",
  "Tag 2",
  "Tag 3",
  "Tag 2",
  "Tag 3",
  "Tag 2",
  "Tag 3",
  "Tag 2",
  "Tag 3",
  "Tag 2",
  "Tag 3",
  "Tag 2",
  "Tag 3",
  "Tag 2",
  "Tag 3",
  "Tag 2",
  "Tag 3",
  "Tag 2",
  "Tag 3",
  "Tag 2",
  "Tag 3",
  "Tag 2",
  "Tag 3",
  "Tag 2",
  "Tag 3",
]);
const inputVisible = ref(false);
const InputRef = ref();

const handleClose = (tag) => {
  dynamicTags.value.splice(dynamicTags.value.indexOf(tag), 1);
};

const showInput = () => {
  inputVisible.value = true;
  nextTick(() => {
    console.log();
  });
};

const handleInputConfirm = () => {
  if (inputValue.value) {
    dynamicTags.value.push(inputValue.value);
  }
  inputVisible.value = false;
  inputValue.value = "";
};
</script>

<style scoped></style>
