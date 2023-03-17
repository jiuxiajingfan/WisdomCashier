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
        <el-button type="primary" @click="add" :loading="loding">
          确定
        </el-button>
      </span>
    </template>
  </el-dialog>
  <el-dialog v-model="dialogVisible2" title="删除标签" width="30%">
    <span>确认删除该分类吗？</span>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible2 = false">取消</el-button>
        <el-button type="primary" @click="del"> 确认 </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { nextTick, onMounted, ref } from "vue";
import { ElInput } from "element-plus";
import api from "@/api/api";
import router from "@/router";
import utils from "@/utils/utils";
const inputmodel = ref("");
const dialogVisible = ref(false);
const dialogVisible2 = ref(false);
const loding = ref(false);
const delc = ref("");
const add = () => {
  loding.value = true;
  api
    .get("Shop/addCategory", {
      params: {
        sid: router.currentRoute.value.query.id,
        category: inputmodel.value.trim(),
      },
    })
    .then((res) => {
      dialogVisible.value = false;
      inputmodel.value = "";
      utils.showMessage(res.data.code, res.data.msg);
      flush();
    })
    .finally(() => {
      loding.value = false;
    });
};
const flush = () => {
  api
    .get("Shop/getCategory", {
      params: {
        sid: router.currentRoute.value.query.id,
      },
    })
    .then((res) => {
      dynamicTags.value = res.data.data;
    });
};
const del = () => {
  loding.value = true;
  api
    .get("Shop/delCategory", {
      params: {
        sid: router.currentRoute.value.query.id,
        category: delc.value,
      },
    })
    .then((res) => {
      flush();
      dialogVisible2.value = false;
      utils.showMessage(res.data.code, res.data.msg);
    })
    .finally(() => {
      loding.value = false;
    });
};
onMounted(() => {
  flush();
});
const dynamicTags = ref([]);

const handleClose = (tag) => {
  delc.value = tag;
  dialogVisible2.value = true;
};
</script>

<style scoped></style>
