<template>
  <el-row>
    <el-col :span="24">
      <h2 style="font-size: 30px">资料设置</h2>
    </el-col>
  </el-row>
  <el-row style="margin-top: 5%">
    <el-col :span="1" />
    <el-col :span="11">
      <el-card style="height: calc(50vh)">
        <template #header>
          <div class="card-header">
            <h1>基础信息</h1>
          </div>
        </template>
        <el-row>
          <el-col :span="24">
            <el-collapse v-model="activeNames" accordion>
              <el-collapse-item name="1">
                <template #title>
                  <el-icon size="20px" style="color: #1e9fff">
                    <Tickets />
                  </el-icon>
                  <span
                    style="margin-left: 5px; font-size: 20px; color: #1e9fff"
                    >店铺信息</span
                  >
                </template>
                <el-descriptions
                  direction="horizontal"
                  column="1"
                  size="default"
                  border
                >
                  <el-descriptions-item
                    label="店铺编号"
                    label-align="center"
                    align="center"
                  >
                    {{ data.id }}
                  </el-descriptions-item>
                  <el-descriptions-item
                    label="店铺名称"
                    label-align="center"
                    align="center"
                  >
                    {{ data.shopName }}
                  </el-descriptions-item>
                  <el-descriptions-item
                    label="注册时间"
                    label-align="center"
                    align="center"
                  >
                    {{ data.createTime }}
                  </el-descriptions-item>
                  <el-descriptions-item
                    label="店铺描述"
                    label-align="center"
                    align="center"
                    >{{ data.desc }}
                  </el-descriptions-item>
                </el-descriptions>
              </el-collapse-item>
              <el-collapse-item name="2">
                <template #title>
                  <el-icon size="20px" style="color: #1e9fff"
                    ><Setting
                  /></el-icon>
                  <span
                    style="margin-left: 5px; font-size: 20px; color: #1e9fff"
                    >信息修改</span
                  >
                </template>
                <el-row>
                  <el-col :span="5" />
                  <el-col :span="12">
                    <el-form label-position="left">
                      <el-form-item>
                        <template #label>
                          <span style="font-size: 15px">店铺名称:</span>
                        </template>
                        <el-input
                          style="width: 300px"
                          v-model="name"
                          clearable
                        ></el-input>
                      </el-form-item>
                      <el-form-item>
                        <template #label>
                          <span style="font-size: 15px">店铺描述:</span>
                        </template>
                        <el-input
                          v-model="desc"
                          style="width: 300px"
                          type="textarea"
                          clearable
                          :autosize="{ minRows: 2, maxRows: 10 }"
                        ></el-input>
                      </el-form-item>
                      <el-button type="primary" @click="post" :loading="lod"
                        >确定</el-button
                      >
                    </el-form>
                  </el-col>
                </el-row>
              </el-collapse-item>
            </el-collapse>
          </el-col>
        </el-row>
      </el-card>
    </el-col>
    <el-space></el-space>
    <el-col :span="11">
      <el-card style="height: calc(50vh)">
        <template #header>
          <div class="card-header">
            <h1>签约信息</h1>
          </div>
        </template>
        <el-descriptions
          direction="horizontal"
          column="1"
          size="default"
          border
        >
          <el-descriptions-item
            label="支付宝收款状态"
            label-align="center"
            align="center"
          >
            <div v-show="data.zfb === 0">
              <el-icon style="color: red"><CircleCloseFilled /></el-icon>
              待授权
              <el-button type="text" @click="diazfb = true">去授权</el-button>
            </div>
            <div v-show="data.zfb === 2">
              <el-icon style="color: red"><CircleCloseFilled /></el-icon>
              授权失效
              <el-button type="text" @click="diazfb = true">去授权</el-button>
            </div>
            <div v-show="data.zfb === 1">
              <el-icon style="color: green"><SuccessFilled /></el-icon>
              可用
            </div>
          </el-descriptions-item>
          <el-descriptions-item
            label="微信收款状态"
            label-align="center"
            align="center"
          >
            <div v-show="data.wx === 0">
              <el-icon style="color: red"><CircleCloseFilled /></el-icon>
              待授权
              <el-button type="text" @click="diavx = true">去授权</el-button>
            </div>
            <div v-show="data.wx === 2">
              <el-icon style="color: red"><CircleCloseFilled /></el-icon>
              授权失效
              <el-button type="text" @click="diavx = true">去授权</el-button>
            </div>
            <div v-show="data.wx === 1">
              <el-icon style="color: green"><SuccessFilled /></el-icon>
              可用
            </div>
          </el-descriptions-item>
          <el-descriptions-item
            label="打印机状态"
            label-align="center"
            align="center"
          >
            <div v-show="1 === 1">
              <el-icon style="color: red"><CircleCloseFilled /></el-icon>
              未连接
            </div>
            <div v-show="1 !== 1">
              <el-icon style="color: green"><SuccessFilled /></el-icon>
              可用
            </div>
          </el-descriptions-item>
          <el-descriptions-item
            label="公斤秤状态"
            label-align="center"
            align="center"
          >
            <div v-show="1 === 1">
              <el-icon style="color: red"><CircleCloseFilled /></el-icon>
              未连接
            </div>
            <div v-show="1 !== 1">
              <el-icon style="color: green"><SuccessFilled /></el-icon>
              可用
            </div>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>
    </el-col>
    <el-col :span="1" />
  </el-row>
  <el-dialog
    v-model="diazfb"
    title="支付宝授权"
    width="60%"
    center
    align-center
  >
    <el-row>
      <el-col :span="5" />
      <el-col :span="19">
        <el-steps
          :space="200"
          :active="active"
          finish-status="success"
          align-center
        >
          <el-step title="扫码" description="请打开支付宝扫描下方二维码" />
          <el-step title="确认" description="请填写资料并完成相应授权" />
          <el-step title="完成" />
        </el-steps>
      </el-col>
    </el-row>
    <el-row style="margin-top: 30px">
      <el-col :span="8" />
      <el-col :span="8" style="margin-left: 3%">
        <div v-show="active === 1">
          <qrcode-vue value="qrCode123" size="300"></qrcode-vue>
        </div>
        <div v-show="active === 2">
          <h2>填写完成后后点击下一步</h2>
        </div>
      </el-col>
    </el-row>
    <template #footer>
      <span class="dialog-footer">
        <el-button
          @click="
            diazfb = false;
            active = 0;
          "
          >取消</el-button
        >
        <el-button type="primary" @click="next">
          <span v-if="active === 0">开始</span>
          <span v-else-if="active === 1 || active === 2">下一步</span>
          <span v-else-if="active === 3">确定</span>
        </el-button>
      </span>
    </template>
  </el-dialog>
  <el-dialog v-model="diavx" title="微信授权" width="60%" center align-center>
    <el-row>
      <el-col :span="5" />
      <el-col :span="19">
        <el-steps
          :space="200"
          :active="active"
          finish-status="success"
          align-center
        >
          <el-step title="扫码" description="请打开微信扫描下方二维码" />
          <el-step title="确认" description="请填写资料并完成相应授权" />
          <el-step title="完成" />
        </el-steps>
      </el-col>
    </el-row>
    <el-row style="margin-top: 30px">
      <el-col :span="8" />
      <el-col :span="8" style="margin-left: 3%">
        <div v-show="active === 1">
          <qrcode-vue value="qrCode123" size="300"></qrcode-vue>
        </div>
        <div v-show="active === 2">
          <h2>填写完成后后点击下一步</h2>
        </div>
      </el-col>
    </el-row>
    <template #footer>
      <span class="dialog-footer">
        <el-button
          @click="
            diavx = false;
            active = 0;
          "
          >取消</el-button
        >
        <el-button type="primary" @click="next2">
          <span v-if="active === 0">开始</span>
          <span v-else-if="active === 1 || active === 2">下一步</span>
          <span v-else-if="active === 3">确定</span>
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import router from "@/router";
import api from "@/api/api";
import utils from "@/utils/utils";
import QrcodeVue from "qrcode.vue";
const diazfb = ref(false);
const diavx = ref(false);
const activeNames = ref(["1"]);
let data = reactive({
  id: "",
  shopName: "",
  desc: "",
  createTime: "",
  wx: "",
  zfb: "",
});
const lod = ref(false);
const post = () => {
  lod.value = true;
  api
    .post("Shop/updateShopMessage", {
      sid: router.currentRoute.value.query.id,
      name: name.value.trim(),
      desc: desc.value.trim(),
    })
    .then((res) => {
      utils.showMessage(res.data.code, res.data.msg);
      refush();
    })
    .finally(() => {
      lod.value = false;
    });
};
const refush = () => {
  api
    .get("Shop/getShopMessageByID", {
      params: {
        sid: router.currentRoute.value.query.id,
      },
    })
    .then((res) => {
      data.zfb = res.data.data.zfb;
      data.wx = res.data.data.wx;
      data.shopName = res.data.data.shopName;
      data.createTime = res.data.data.createTime;
      data.id = res.data.data.id;
      data.desc = res.data.data.desc;
      desc.value = data.desc;
      name.value = data.shopName;
    });
};
let name = ref("");
let desc = ref("");
onMounted(() => {
  refush();
});
const active = ref(0);

const next = () => {
  if (active.value++ > 2) {
    active.value = 0;
    diazfb.value = false;
  }
};
const next2 = () => {
  if (active.value++ > 2) {
    active.value = 0;
    diavx.value = false;
  }
};
</script>

<style scoped></style>
