<template>
  <el-row>
    <el-col :span="8" />
    <el-col :span="8">
      <p class="ff">申请开店</p>
    </el-col>
    <el-col :span="8">
      <el-button type="primary" style="margin-top: 60px" @click="dia1"
        >申请进度
      </el-button>
    </el-col>
  </el-row>
  <el-card class="box-card" style="margin-top: 10px">
    <template #header>
      <div class="card-header">
        <el-steps :active="active" align-center finish-status="success">
          <el-step title="填写店铺信息" />
          <el-step title="上传相关证件" />
          <el-step title="确定信息" />
          <el-step title="完成" />
        </el-steps>
      </div>
    </template>
    <div v-show="active === 0">
      <el-row>
        <el-col :span="4"></el-col>
        <el-col :span="16">
          <el-form
            :model="form"
            label-width="120px"
            ref="fromref"
            :rules="rules"
          >
            <el-form-item label="店铺名" prop="name">
              <el-input v-model="form.name" />
            </el-form-item>
            <el-form-item label="店铺分类">
              <el-select v-model="form.region">
                <el-option label="零售" value="1" />
                <el-option label="五金" value="2" />
                <el-option label="餐饮" value="3" />
              </el-select>
            </el-form-item>
            <el-form-item label="店铺描述" prop="desc">
              <el-input v-model="form.desc" type="textarea" />
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </div>
    <div v-show="active === 1">
      <el-row>
        <el-col :span="4"></el-col>
        <el-col :span="16">
          <el-form
            :model="form"
            label-width="150px"
            :rules="rules"
            ref="fromref2"
          >
            <el-form-item label="社会统一信用代码" prop="resource">
              <el-input v-model="form.resource" />
            </el-form-item>
            <el-form-item label="店铺营业执照" prop="p1">
              <el-upload
                action="#"
                ref="uploadRef"
                list-type="picture-card"
                :before-upload="beforeUpload"
                accept=".jpg,.jpeg,.JPG,.JPEG,.png"
                :auto-upload="false"
                :file-list="fileList"
                :on-change="fileChange"
                :limit="3"
                :class="{ hide: hideUpload }"
              >
                <el-icon>
                  <Plus />
                </el-icon>

                <template #file="{ file }">
                  <div>
                    <img :src="file.url" alt="" />
                    <span class="el-upload-list__item-actions">
                      <span
                        class="el-upload-list__item-preview"
                        @click="handlePictureCardPreview(file)"
                      >
                        <el-icon><zoom-in /></el-icon>
                      </span>
                      <span
                        v-if="!disabled"
                        class="el-upload-list__item-delete"
                        @click="handleRemove(file)"
                      >
                        <el-icon><Delete /></el-icon>
                      </span>
                    </span>
                  </div>
                </template>
              </el-upload>
              <el-dialog v-model="dialogVisible">
                <img w-full :src="dialogImageUrl" alt="Preview Image" />
              </el-dialog>
            </el-form-item>
            <el-form-item label="执照人身份证正面" prop="p2">
              <el-upload
                action="#"
                ref="uploadRef2"
                list-type="picture-card"
                accept=".jpg,.jpeg,.JPG,.JPEG"
                :auto-upload="false"
                :file-list="fileList2"
                :on-change="fileChange2"
                :limit="3"
                :class="{ hide: hideUpload2 }"
                :before-upload="beforeUpload2"
              >
                <el-icon>
                  <Plus />
                </el-icon>

                <template #file="{ file }">
                  <div>
                    <img :src="file.url" alt="" />
                    <span class="el-upload-list__item-actions">
                      <span
                        class="el-upload-list__item-preview"
                        @click="handlePictureCardPreview(file)"
                      >
                        <el-icon><zoom-in /></el-icon>
                      </span>
                      <span
                        v-if="!disabled"
                        class="el-upload-list__item-delete"
                        @click="handleRemove2(file)"
                      >
                        <el-icon><Delete /></el-icon>
                      </span>
                    </span>
                  </div>
                </template>
              </el-upload>
            </el-form-item>
            <el-form-item label="执照人身份证反面" prop="p3">
              <el-upload
                action="#"
                ref="uploadRef3"
                list-type="picture-card"
                accept=".jpg,.jpeg,.JPG,.JPEG"
                :auto-upload="false"
                :file-list="fileList3"
                :before-upload="beforeUpload3"
                :on-change="fileChange3"
                :limit="3"
                :class="{ hide: hideUpload3 }"
              >
                <el-icon>
                  <Plus />
                </el-icon>

                <template #file="{ file }">
                  <div>
                    <img :src="file.url" alt="" />
                    <span class="el-upload-list__item-actions">
                      <span
                        class="el-upload-list__item-preview"
                        @click="handlePictureCardPreview(file)"
                      >
                        <el-icon><zoom-in /></el-icon>
                      </span>
                      <span
                        v-if="!disabled"
                        class="el-upload-list__item-delete"
                        @click="handleRemove3(file)"
                      >
                        <el-icon><Delete /></el-icon>
                      </span>
                    </span>
                  </div>
                </template>
              </el-upload>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </div>
    <div v-if="active === 2" :key="2">
      <el-descriptions
        direction="horizontal"
        column="1"
        size="default"
        border
        :loading="lod"
      >
        <el-descriptions-item
          label="店铺名称"
          label-align="center"
          align="center"
        >
          {{ form.name }}
        </el-descriptions-item>
        <el-descriptions-item
          label="店铺分类"
          label-align="center"
          align="center"
        >
          {{ form.region }}
        </el-descriptions-item>
        <el-descriptions-item
          label="店铺描述"
          label-align="center"
          align="center"
        >
          {{ form.desc }}
        </el-descriptions-item>
        <el-descriptions-item
          label="社会统一信用代码"
          label-align="center"
          align="center"
        >
          {{ form.resource }}
        </el-descriptions-item>
        <el-descriptions-item
          label="营业执照"
          label-align="center"
          align="center"
        >
          <el-image
            style="width: 400px; height: 200px"
            :src="img1P"
            :zoom-rate="1.2"
            fit="cover"
          />
        </el-descriptions-item>
        <el-descriptions-item
          label="执照人身份证正面"
          label-align="center"
          align="center"
        >
          <el-image
            style="width: 400px; height: 200px"
            :src="img2P"
            :zoom-rate="1.2"
            fit="cover"
          />
        </el-descriptions-item>
        <el-descriptions-item
          label="执照人身份证反面"
          label-align="center"
          align="center"
        >
          <el-image
            style="width: 400px; height: 200px"
            :src="img3P"
            :zoom-rate="1.2"
            fit="cover"
          />
        </el-descriptions-item>
      </el-descriptions>
    </div>
    <div v-if="active === 3">
      <h2>请耐心等待审批结果</h2>
    </div>
    <div>
      <el-button
        v-show="active === 1 || active === 2"
        style="margin-top: 12px"
        @click="next(-1)"
        :loading="lod"
        >上一步
      </el-button>
      <el-button
        v-if="active === 0 || active === 1"
        style="margin-top: 12px"
        @click="next(1)"
        type="primary"
        :loading="lod"
        :disabled="disable"
      >
        <span v-show="!disable">下一步</span>
        <span v-show="disable">请等待上一份申请完成</span>
      </el-button>
      <el-button
        v-if="active === 2"
        style="margin-top: 12px"
        @click="submitUpload"
        type="primary"
        :loading="lod"
        >确定
      </el-button>
      <el-button
        v-if="active === 3"
        style="margin-top: 12px"
        @click="next(1)"
        type="primary"
        :loading="lod"
        >完成
      </el-button>
    </div>
  </el-card>
  <el-dialog
    v-model="dialogTableVisible"
    title="申请列表"
    width="50%"
    align-center="true"
  >
    <el-table :data="applyData" height="calc(30vh)">
      <el-table-column property="name" label="店铺名" width="auto" />
      <el-table-column label="申请状态" width="auto">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <i :style="getStyle(scope.row.status - 1)"></i>
            <span style="margin-left: 2px">
              {{ tradetype2[scope.row.status - 1].msg }}
            </span>
          </div>
        </template>
      </el-table-column>
      <el-table-column property="gmtCreate" label="申请时间" width="auto" />
      <el-table-column property="tips" label="备注" width="auto" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-popconfirm
            title="确定要删除吗?"
            @confirm="deleteGood(scope.row.id)"
          >
            <template #reference>
              <el-button v-show="scope.row.status === 1" link type="primary"
                >撤销
              </el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
  </el-dialog>
</template>
<script setup>
import { onBeforeMount, reactive, ref } from "vue";

const dialogTableVisible = ref(false);
const form = reactive({
  name: "",
  region: "",
  resource: "",
  desc: "",
  img1: "",
  img2: "",
  img3: "",
});
let hideUpload = ref(false);
let hideUpload2 = ref(false);
let hideUpload3 = ref(false);
const lod = ref(false);
const active = ref(0);
const next = (res) => {
  if (res > 0) {
    if (active.value === 0) {
      fromref.value.validate((valid) => {
        if (valid) {
          active.value += res;
        }
      });
    } else if (active.value === 1) {
      fromref2.value.validate((valid) => {
        if (valid) {
          active.value += res;
        }
      });
    }
  } else {
    active.value += res;
  }
  if (active.value > 2 || active.value < 0) active.value = 0;
};

const dialogImageUrl = ref("");
const dialogVisible = ref(false);
const disabled = ref(false);
const fileList = ref([]);
const fileList2 = ref([]);
const fileList3 = ref([]);

const handleRemove = (file) => {
  const list = fileList.value;
  for (const i in list) {
    if (list[i].uid === file.uid) {
      list.splice(i, 1);
    }
  }
  fileList.value = list;
  if (fileList.value.length === 0) {
    hideUpload.value = false;
    valiIconFlag1 = false;
  }
};
const handleRemove2 = (file) => {
  const list = fileList2.value;
  for (const i in list) {
    if (list[i].uid === file.uid) {
      list.splice(i, 1);
    }
  }
  fileList2.value = list;
  if (fileList2.value.length === 0) {
    hideUpload2.value = false;
    valiIconFlag2 = false;
  }
};
const handleRemove3 = (file) => {
  const list = fileList3.value;
  for (const i in list) {
    if (list[i].uid === file.uid) {
      list.splice(i, 1);
    }
  }
  fileList3.value = list;
  if (fileList3.value.length === 0) {
    hideUpload3.value = false;
    valiIconFlag3 = false;
  }
};

const handlePictureCardPreview = (file) => {
  dialogImageUrl.value = file.url;
  dialogVisible.value = true;
};
const fileChange = (file, resfileList) => {
  fileList.value = resfileList;
  if (fileList.value.length > 0) {
    img1P.value = fileList.value[0].url;
    hideUpload.value = true;
    valiIconFlag1 = true;
  }
};
const fileChange2 = (file, resfileList) => {
  fileList2.value = resfileList;
  if (fileList.value.length > 0) {
    img2P.value = fileList2.value[0].url;
    hideUpload2.value = true;
    valiIconFlag2 = true;
  }
};
const fileChange3 = (file, resfileList) => {
  fileList3.value = resfileList;
  if (fileList.value.length > 0) {
    img3P.value = fileList3.value[0].url;
    hideUpload3.value = true;
    valiIconFlag3 = true;
  }
};
import api from "@/api/api";
import utils from "@/utils/utils";

const uploadRef = ref();
const uploadRef2 = ref();
const uploadRef3 = ref();
let img1True = 0;
let img2True = 0;
let img3True = 0;
const sleep = (timeout) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve();
    }, timeout);
  });
};
const submitUpload = async () => {
  lod.value = true;
  let cnt = 1;
  while (img1True !== 1 && img1True !== 1 && img1True !== 1) {
    if (img1True !== 1) uploadRef.value.submit();
    if (img2True !== 1) uploadRef2.value.submit();
    if (img3True !== 1) uploadRef3.value.submit();
    await sleep(2000);
    cnt++;
    if (cnt === 5) {
      utils.showMessage(400, "上传失败,请重试");
      return;
    }
  }
  api
    .post("Shop/applyShop", {
      name: form.name,
      descript: form.desc,
      imgShop: img1P.value,
      imgIdcard1: img2P.value,
      imgIdcard2: img3P.value,
      code: form.resource,
    })
    .then((res) => {
      utils.showMessage(res.data.code, res.data.msg);
      if (res.data.code === 200) {
        active.value = 3;
        img1True = 0;
        img3True = 0;
        img2True = 0;
        form.resource = "";
        form.desc = "";
        form.name = "";
        form.img3 = "";
        form.img2 = "";
        form.img1 = "";
        form.region = "";
        hideUpload.value = false;
        hideUpload2.value = false;
        hideUpload3.value = false;
        fileList.value = [];
        fileList2.value = [];
        fileList3.value = [];
      }
    })
    .finally(() => {
      lod.value = false;
    });
};
const beforeUpload = (file, id) => {
  let fd = new FormData();
  fd.append("file", file);
  api
    .post("/file/upload", fd, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    })
    .then((res) => {
      if (res.data.code != 200) {
        utils.showErrMessage(res.data.msg);
      }
      img1P.value = res.data.msg;
      img1True = 1;
    });
  return false;
};
const beforeUpload2 = (file, id) => {
  let fd = new FormData();
  fd.append("file", file);
  api
    .post("/file/upload", fd, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    })
    .then((res) => {
      if (res.data.code != 200) {
        utils.showErrMessage(res.data.msg);
      }
      img2P.value = res.data.msg;
      img2True = 1;
    });
  return false;
};
const beforeUpload3 = (file, id) => {
  let fd = new FormData();
  fd.append("file", file);
  api
    .post("/file/upload", fd, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    })
    .then((res) => {
      if (res.data.code != 200) {
        utils.showErrMessage(res.data.msg);
      }
      img3P.value = res.data.msg;
      img3True = 1;
    });
  return false;
};
const img1P = ref("");
const img2P = ref("");
const img3P = ref("");
const fromref = ref();
const fromref2 = ref();
let valiIconFlag1 = false;
let valiIconFlag2 = false;
let valiIconFlag3 = false;

let valiIcon1 = (rule, value, callback) => {
  if (!valiIconFlag1) {
    callback(new Error("请上传"));
  } else {
    callback();
  }
};
let valiIcon2 = (rule, value, callback) => {
  if (!valiIconFlag2) {
    callback(new Error("请上传"));
  } else {
    callback();
  }
};
let valiIcon3 = (rule, value, callback) => {
  if (!valiIconFlag3) {
    callback(new Error("请上传"));
  } else {
    callback();
  }
};
const rules = reactive({
  name: [
    { required: true, message: "请输入", trigger: "blur" },
    {
      min: 1,
      max: 20,
      message: "请正确输入",
      trigger: "blur",
    },
  ],
  region: [
    { required: true, message: "请输入", trigger: "blur" },
    {
      min: 1,
      max: 20,
      message: "请正确输入",
      trigger: "blur",
    },
  ],
  resource: [
    { required: true, message: "请输入", trigger: "blur" },
    {
      min: 1,
      max: 20,
      message: "请正确输入",
      trigger: "blur",
    },
  ],
  desc: [
    { required: true, message: "请输入", trigger: "blur" },
    {
      min: 1,
      max: 200,
      message: "请正确输入",
      trigger: "blur",
    },
  ],
  p1: [{ required: true, validator: valiIcon1 }],
  p2: [{ required: true, validator: valiIcon2 }],
  p3: [{ required: true, validator: valiIcon3 }],
});
const disable = ref(false);
const dia1 = () => {
  dialogTableVisible.value = true;
  api.post("shop/getApply").then((res) => {
    applyData.value = res.data.data.list;
    disable.value = res.data.data.flag;
  });
};
let applyData = ref([]);
onBeforeMount(() => {
  api.post("shop/getApply").then((res) => {
    applyData.value = res.data.data.list;
    disable.value = res.data.data.flag;
  });
});
let tradetype2 = [
  { msg: "待审批", color: "#fffb09" },
  { msg: "通过", color: "#00ff08" },
  { msg: "拒绝", color: "#ff0000" },
  { msg: "撤销", color: "#04f5ea" },
];
const deleteGood = (data) => {
  api
    .get("Shop/cancelApply", {
      params: {
        id: data,
      },
    })
    .then((res) => {
      utils.showMessage(res.data.code, res.data.msg);
      api.post("shop/getApply").then((res) => {
        applyData.value = res.data.data.list;
        disable.value = res.data.data.flag;
      });
    });
};
const getStyle = (data) => {
  return (
    "background-color: " +
    tradetype2[data].color +
    ";\n" +
    "width: 15px;\n" +
    "height: 15px;\n" +
    "border-radius: 50%;\n" +
    "display: block;"
  );
};
</script>

<style lang="scss">
.hide .el-upload--picture-card {
  display: none;
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
