<template>
  <div class="common-layout">
    <el-container>
      <el-container>
        <el-aside width="600px" class="aside">
          <el-scrollbar class="scr1">
            <el-input
              placeholder="请输入要搜索的商品名或条形码"
              v-model="searchText"
              class="input-with-select"
              @keyup.enter="queryTaskList"
              v-loading="lod"
            >
              <template #append>
                <el-button
                  icon="Search"
                  @click="queryTaskList"
                  :disabled="searchText.trim().length === 0"
                  :loading="lod"
                  >搜索
                </el-button>
              </template>
            </el-input>
            <el-button
              type="primary"
              style="margin-left: 10px"
              @click="dialogVisiblegd = true"
              >挂单
            </el-button>
            <el-dialog v-model="dialogVisiblegd" title="挂单" width="30%">
              <h2 style="font-size: 30px">如需备注请输入备注</h2>
              <el-input v-model="tips" @keyup.enter="hangclick"></el-input>
              <template #footer>
                <span class="dialog-footer">
                  <el-button @click="dialogVisiblegd = false">取消</el-button>
                  <el-button type="primary" @click="hangclick">确定 </el-button>
                </span>
              </template>
            </el-dialog>
            <el-table
              :data="tradeList"
              @row-click="onRowClick"
              show-summary
              height="calc(100vh - 170px)"
              :summary-method="getSummaries"
            >
              <el-table-column label="名称" prop="name"></el-table-column>
              <el-table-column label="编号" prop="gid"></el-table-column>
              <el-table-column label="数量" prop="num"></el-table-column>
              <el-table-column label="单价" prop="priceOut"></el-table-column>
              <el-table-column prop="priceVip" label="会员价" width="auto" />
              <el-table-column label="总价">
                <template v-slot="scope">
                  {{
                    math
                      .multiply(
                        math.bignumber(scope.row.num),
                        math.bignumber(
                          isVip === 1 ? scope.row.priceVip : scope.row.priceOut
                        )
                      )
                      .toFixed(2)
                  }}
                </template>
              </el-table-column>
            </el-table>
          </el-scrollbar>
          <el-drawer
            v-model="drawer"
            title="挂单详情"
            direction="ltr"
            size="60%"
          >
            <el-table :data="hangList" style="width: 100%">
              <el-table-column type="expand">
                <template #default="props">
                  <div m="4">
                    <el-table :data="props.row.list">
                      <el-table-column
                        label="名称"
                        prop="name"
                      ></el-table-column>
                      <el-table-column
                        label="编号"
                        prop="gid"
                      ></el-table-column>
                      <el-table-column
                        label="数量"
                        prop="num"
                      ></el-table-column>
                      <el-table-column
                        label="单价"
                        prop="priceOut"
                      ></el-table-column>
                      <el-table-column label="总价">
                        <template v-slot="scope">
                          {{
                            math
                              .multiply(
                                math.bignumber(scope.row.num),
                                math.bignumber(scope.row.priceOut)
                              )
                              .toFixed(2)
                          }}
                        </template>
                      </el-table-column>
                    </el-table>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="tip" label="备注" width="180" />
              <el-table-column prop="sum" label="金额" width="180" />
              <el-table-column prop="time" label="日期" />
              <el-table-column fixed="right" label="操作" width="120">
                <template v-slot="scope">
                  <el-popconfirm
                    title="确定要删除吗?"
                    @confirm="deleHang(scope.row.id)"
                  >
                    <template #reference>
                      <el-button link type="primary">删除</el-button>
                    </template>
                  </el-popconfirm>
                  <el-button
                    link
                    type="primary"
                    @click="getHangon(scope.row.id)"
                    >取单
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-drawer>
          <el-drawer
            v-model="drawer2"
            title="最近十笔交易记录"
            direction="rtl"
            size="60%"
          >
            <el-table :data="trdeList" style="width: 100%">
              <el-table-column prop="id" label="流水号" width="180" />
              <el-table-column label="付款方式" width="180">
                <template #default="scope">
                  <span style="margin-left: 2px">
                    {{ tradetype[scope.row.type - 1] }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="income" label="金额" width="180" />
              <el-table-column prop="status" label="交易状态" width="180">
                <template #default="scope">
                  <el-tooltip
                    class="box-item"
                    effect="dark"
                    :content="scope.row.msg"
                    placement="top"
                  >
                    <div style="display: flex; align-items: center">
                      <i :style="getStyle(scope.row.status)"></i>
                      <span style="margin-left: 2px">
                        {{ tradetype1[scope.row.status].msg }}
                      </span>
                    </div>
                  </el-tooltip>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="日期" />
            </el-table>
          </el-drawer>
        </el-aside>
        <el-container>
          <el-main>
            <div v-show="picshow">
              <div class="demo-fit">
                <el-avatar
                  shape="square"
                  :size="200"
                  fit="fit"
                  :src="lastData.picUrl + '?' + new Date().getTime()"
                  style="margin-top: 30px"
                />
              </div>
              <div class="h2">
                <div style="margin-top: 100px">
                  <h2 style="font-size: 30px">
                    {{ lastData.name }}
                  </h2>
                </div>
                <div style="margin-top: 30px">
                  <h2>
                    {{ lastData.metrology }}
                  </h2>
                </div>
                <div style="margin-top: 30px">
                  <label>数量:</label>
                  <el-input-number
                    style="margin-left: 20px"
                    v-model="lastData.num"
                    :min="1"
                    :max="100"
                  />
                </div>
                <div style="margin-top: 30px">
                  <label>单价:</label>
                  <el-input-number
                    style="margin-left: 20px"
                    v-model="lastData.priceOut"
                    :min="1"
                    :max="100"
                    :precision="2"
                    :disabled="lastData.priceOut != ''"
                  />
                </div>
                <div style="margin-top: 60px">
                  <el-button size="large" @click="dele(lastData.gid)"
                    >删除
                  </el-button>
                  <el-button
                    type="primary"
                    size="large"
                    style="margin-left: 20px"
                    @click="sure(lastData.gid)"
                    >确定
                  </el-button>
                </div>
              </div>
            </div>
            <el-empty
              v-show="!picshow"
              image-size="400"
              description="暂无商品"
            />
          </el-main>
          <el-footer>
            <el-scrollbar>
              <el-badge
                :value="Hang.get.length"
                class="item"
                :hidden="Hang.get.length === 0"
              >
                <el-button
                  size="large"
                  style="height: 80%; margin-top: 5px"
                  @click="drawer = !drawer"
                >
                  <save
                    theme="outline"
                    size="28"
                    fill="#333"
                    :strokeWidth="1"
                    style="margin-right: 10px"
                  />
                  挂单详情
                </el-button>
              </el-badge>
              <el-button
                size="large"
                style="margin-left: 30px; margin-top: 5px; height: 80%"
                @click="vipcheckf"
              >
                <inspection
                  theme="outline"
                  size="28"
                  fill="#333"
                  :strokeWidth="1"
                  style="margin-right: 10px"
                />
                会员验证
              </el-button>
              <el-button
                size="large"
                style="margin-left: 30px; margin-top: 5px; height: 80%"
                @click="onMonery"
                :disabled="Trade.get.length === 0"
              >
                <paper-money
                  theme="outline"
                  size="28"
                  fill="#333"
                  :strokeWidth="1"
                  style="margin-right: 10px"
                />
                现金结算
              </el-button>
              <el-button
                size="large"
                style="margin-left: 30px; margin-top: 5px; height: 80%"
                @click="dialogVisiblezfb = true"
                :disabled="Trade.get.length === 0 || status[1] === 0"
              >
                <alipay
                  theme="outline"
                  size="28"
                  fill="#333"
                  :strokeWidth="1"
                  style="margin-right: 10px"
                />
                支付宝结算
              </el-button>
              <el-button
                size="large"
                style="margin-left: 30px; margin-top: 5px; height: 80%"
                :disabled="Trade.get.length === 0 || status[0] === 0"
              >
                <wechat
                  theme="outline"
                  size="28"
                  fill="#333"
                  :strokeWidth="1"
                  style="margin-right: 10px"
                />
                微信结算
              </el-button>
              <el-button
                size="large"
                style="margin-left: 30px; margin-top: 5px; height: 80%"
                @click="leastFun"
              >
                <transaction-order
                  theme="outline"
                  size="28"
                  fill="#333"
                  :strokeWidth="1"
                  style="margin-right: 10px"
                />
                最近交易
              </el-button>
            </el-scrollbar>
            <el-dialog
              v-model="dialogVisiblezfb"
              title="支付宝支付"
              width="30%"
              @focus="this.$refs['zfbinput'].focus()"
            >
              <h2 style="font-size: 30px">请扫描或输入顾客付款条形码</h2>
              <el-input
                ref="zfbinput"
                v-model="userPayID"
                @keyup.enter="alipayP"
              ></el-input>
              <template #footer>
                <span class="dialog-footer">
                  <el-button @click="dialogVisiblezfb = false">取消</el-button>
                  <el-button type="primary" @click="alipayP"> 确定 </el-button>
                </span>
              </template>
            </el-dialog>
            <el-dialog
              v-model="dialogFormVisible"
              title="新增商品"
              width="900px"
              center
            >
              <el-form
                :model="form"
                :inline="true"
                label-position="left"
                style="margin-left: 10%"
                ref="formref"
                :rules="rules"
              >
                <div>
                  <el-form-item
                    label="商品名"
                    :label-width="formLabelWidth"
                    prop="name"
                  >
                    <el-input style="width: 190px" v-model="form.name" />
                  </el-form-item>
                  <el-form-item
                    label="商品条码"
                    :label-width="formLabelWidth"
                    prop="gid"
                  >
                    <el-input style="width: 190px" v-model="form.gid" />
                  </el-form-item>
                </div>
                <div>
                  <el-form-item
                    label="进价"
                    :label-width="formLabelWidth"
                    prop="price_in"
                  >
                    <el-input style="width: 190px" v-model="form.price_in" />
                  </el-form-item>
                  <el-form-item
                    label="售价"
                    :label-width="formLabelWidth"
                    prop="price_out"
                  >
                    <el-input style="width: 190px" v-model="form.price_out" />
                  </el-form-item>
                  <el-form-item
                    label="会员价"
                    :label-width="formLabelWidth"
                    prop="price_vip"
                  >
                    <el-input style="width: 190px" v-model="form.price_vip" />
                  </el-form-item>
                  <el-form-item label="商品分类" :label-width="formLabelWidth">
                    <el-select v-model="form.type" style="width: 190px">
                      <el-option
                        v-for="item in options"
                        :key="item"
                        :label="item"
                        :value="item"
                      />
                    </el-select>
                  </el-form-item>
                </div>
                <div>
                  <el-form-item
                    label="数量"
                    :label-width="formLabelWidth"
                    prop="num"
                  >
                    <el-input style="width: 190px" v-model.number="form.num" />
                  </el-form-item>
                  <el-form-item label="单位" :label-width="formLabelWidth">
                    <el-input style="width: 190px" v-model="form.metrology" />
                  </el-form-item>
                </div>
                <div>
                  <el-form-item label="生产日期" :label-width="formLabelWidth">
                    <div class="block">
                      <el-date-picker
                        v-model="form.date"
                        type="date"
                        placeholder="请选择生产日期"
                        style="width: 190px"
                        value-format="YYYY-MM-DD"
                      />
                    </div>
                  </el-form-item>
                  <el-form-item
                    label="保质期(天)"
                    :label-width="formLabelWidth"
                    prop="shelfLife"
                  >
                    <el-input
                      style="width: 190px"
                      v-model.number="form.shelfLife"
                    />
                  </el-form-item>
                </div>
              </el-form>
              <template #footer>
                <span class="dialog-footer">
                  <el-button @click="dialogFormVisible = false">离开</el-button>
                  <el-button type="primary" @click="save2"> 保存 </el-button>
                </span>
              </template>
            </el-dialog>
            <el-dialog
              v-model="dialogFormVisible2"
              title="未查询到商品"
              width="600px"
              center
            >
              <h1>未找到该商品信息，是否需要添加该商品？</h1>
              <template #footer>
                <span class="dialog-footer">
                  <el-button @click="leave"> 离开 </el-button>
                  <el-button type="primary" @click="openadd2">添加</el-button>
                </span>
              </template>
            </el-dialog>
            <el-dialog
              v-model="moneyCharge"
              title="现金收款"
              :center="true"
              align-center
              width="40%"
            >
              <el-row>
                <el-col :span="8" />
                <el-col :span="16">
                  <h2>应收款： {{ sumM.toFixed(2) }} 元</h2>
                  <div>
                    <label style="font-size: 20px">实际付款：</label>
                    <el-input
                      style="margin-left: 20px; width: 300px"
                      v-model="giveMoney"
                    ></el-input>
                  </div>
                  <h2>找零：{{ (giveMoney - sumM).toFixed(2) }}</h2>
                </el-col>
              </el-row>
              <template #footer>
                <span class="dialog-footer">
                  <el-button
                    @click="
                      moneyCharge = false;
                      giveMoney = '';
                    "
                  >
                    离开
                  </el-button>
                  <el-button
                    type="primary"
                    @click="buy(1, '', '')"
                    :loading="lod"
                    >确认</el-button
                  >
                </span>
              </template>
            </el-dialog>
            <el-dialog
              v-model="vipcheck"
              title="会员验证 "
              :center="true"
              align-center
              width="30%"
            >
              <el-row>
                <el-col :span="8" />
                <el-col :span="16">
                  <h2>请扫描或输入会员号</h2>
                </el-col>
                <el-col :span="5" />
                <el-col :span="16">
                  <el-input
                    style="margin-left: 20px; width: 300px"
                    v-model="vipNo"
                    @keydown.enter="vipcheckFun"
                  ></el-input>
                </el-col>
              </el-row>
              <template #footer>
                <span class="dialog-footer">
                  <el-button
                    @click="
                      vipcheck = false;
                      vipNo = '';
                    "
                  >
                    离开
                  </el-button>
                  <el-button type="primary" @click="vipcheckFun" :loading="lod"
                    >确认</el-button
                  >
                </span>
              </template>
            </el-dialog>
          </el-footer>
        </el-container>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { nextTick, onBeforeMount, reactive, ref } from "vue";
import api from "@/api/api";
import { useRouter } from "vue-router/dist/vue-router";
import * as math from "mathjs";
import utils from "@/utils/utils";
import {
  Wechat,
  Alipay,
  PaperMoney,
  Save,
  TransactionOrder,
  Inspection,
} from "@icon-park/vue-next";
import Utils from "@/utils/utils";
import { ElLoading, ElNotification } from "element-plus";
import pinia from "@/store/store";
import { storeToRefs } from "pinia/dist/pinia";
import { useHangStore } from "@/store/hangon";
import { useTradeStore } from "@/store/trade";

const Hang = useHangStore(pinia);
const Trade = useTradeStore(pinia);
const { hangList } = storeToRefs(Hang);
const { tradeList } = storeToRefs(Trade);
let searchText = ref("");
const router = useRouter();
let dialogVisiblezfb = ref(false);
let picshow = ref(false);
let lastData = reactive({
  name: "",
  num: 1,
  priceOut: "",
  priceIn: "",
  priceVip: "",
  picUrl: "",
  metrology: "",
  gid: "",
  type: "",
});
const leave = () => {
  dialogFormVisible2.value = false;
  form.name = "";
  form.gid = "";
  form.price_in = 0;
  form.price_out = 0;
  form.num = 0;
  form.shelfLife = "";
  form.date = "";
  form.metrology = "";
  form.profit = 0;
  form.type = "";
};
var reg = /^[0-9]*$/;
const openadd2 = () => {
  dialogFormVisible2.value = false;
  openadd();
};
const lod = ref(false);
const openadd = () => {
  if (reg.test(searchText.value)) {
    api
      .get("/Goods/reqGood", {
        params: {
          gid: searchText.value.toString(),
        },
      })
      .then((res) => {
        form.name = res.data.data.name;
        form.gid = res.data.data.gid;
        form.price_out = res.data.data.priceOut;
        form.num = 1;
        form.metrology = res.data.data.metrology;
      });
  } else {
    form.name = searchText.value;
  }
  dialogFormVisible.value = true;
};
const vipcheckFun = () => {
  lod.value = true;
  api
    .get("Shop/isVip", {
      params: {
        sid: router.currentRoute.value.query.id,
        phone: vipNo.value,
      },
    })
    .then((res) => {
      if (res.data.data === 1) {
        utils.showMessage(200, "校验成功，是会员用户");
        isVip.value = 1;
        vipcheck.value = false;
      } else {
        utils.showMessage(400, "校验失败，非会员用户或会员已过期");
        vipNo.value = "";
      }
    })
    .finally((res) => {
      lod.value = false;
    });
};
const queryTaskList = () => {
  lod.value = true;
  api
    .get("/Goods/getGood", {
      params: {
        gid: searchText.value.trim(),
        sid: router.currentRoute.value.query.id,
      },
    })
    .then((res) => {
      if (res.data.code === 200) {
        Trade.add({
          num: 1,
          name: res.data.data.name,
          metrology: res.data.data.metrology,
          picUrl: res.data.data.picUrl,
          priceOut: res.data.data.priceOut,
          priceIn: res.data.data.priceIn,
          priceVip: res.data.data.priceVip,
          gid: searchText.value.trim(),
          type: res.data.data.type,
        });
        lastData.num = Trade.get.filter((e) => {
          return e.gid === searchText.value.trim();
        })[0].num;
        lastData.name = res.data.data.name;
        lastData.metrology = res.data.data.metrology;
        lastData.picUrl = res.data.data.picUrl;
        lastData.priceOut = res.data.data.priceOut;
        lastData.priceIn = res.data.data.priceIn;
        lastData.priceVip = res.data.data.priceVip;
        lastData.gid = searchText.value.trim();
        lastData.type = res.data.data.type;
        searchText.value = "";
        picshow.value = true;
      } else {
        dialogFormVisible2.value = true;
      }
    })
    .finally(() => {
      lod.value = false;
    });
};
const sure = (gid) => {
  Trade.setOne(gid, lastData.num);
  searchText.value = "";
  picshow.value = false;
  utils.showMessage(200, "修改数量成功！");
};
let userPayID = ref("");
const onRowClick = (row, column, event) => {
  lastData.num = row.num;
  lastData.name = row.name;
  lastData.metrology = row.metrology;
  lastData.picUrl = row.picUrl;
  lastData.priceOut = row.priceOut;
  lastData.priceIn = row.priceIn;
  lastData.priceVip = row.priceVip;
  lastData.type = row.type;
  lastData.gid = row.gid;
  picshow.value = true;
};
const dele = (gid) => {
  Trade.del(gid);
  searchText.value = "";
  picshow.value = false;
  utils.showMessage(200, "删除商品成功！");
};
const moneyCharge = ref(false);
const vipcheck = ref(false);
const isVip = ref(0);
const giveMoney = ref(0);
let sumM = ref(0);
const onMonery = () => {
  moneyCharge.value = true;
};
const vipcheckf = () => {
  vipcheck.value = true;
};

const buy = (type, no, remoteID) => {
  lod.value = true;
  api
    .post("/Goods/buyGood", {
      goods: Trade.get,
      type: type,
      sum: sumM.value,
      sid: router.currentRoute.value.query.id,
      remoteNo: no,
      status: 3,
      id: remoteID,
      vip: isVip.value,
      phone: vipNo.value,
    })
    .then((res) => {
      if (res.data.code === 200) {
        if (type === 1) moneyCharge.value = false;
        utils.showMessage(res.data.code, "结算成功！");
        Trade.clear();
        sumM.value = 0;
        giveMoney.value = 0;
        picshow.value = false;
        isVip.value = 0;
      } else {
        utils.showMessage(res.data.code, "结算失败请重试或联系管理员！");
      }
    })
    .finally(() => {
      lod.value = false;
    });
};
let dialogFormVisible = ref(false);
const dialogFormVisible2 = ref(false);
const formLabelWidth = "140px";
const options = ref([]);
const save2 = () => {
  formref.value.validate((valid) => {
    if (valid) {
      api
        .post("/Goods/addGood", {
          name: form.name,
          gid: form.gid,
          priceIn: form.price_in,
          priceOut: form.price_out,
          priceVip: form.price_vip,
          sid: form.sid,
          date: form.date,
          shelfLife: form.shelfLife,
          num: form.num,
          profit: form.price_out - form.price_in,
          metrology: form.metrology,
          type: form.type,
        })
        .then((res) => {
          Utils.showMessage(res.data.code, res.data.msg);
          if (res.data.code === 200) {
            searchText.value = form.gid;
            dialogFormVisible.value = false;
            form.name = "";
            form.gid = "";
            form.price_in = 0;
            form.price_out = 0;
            form.num = 0;
            form.shelfLife = "";
            form.date = "";
            form.metrology = "";
            form.profit = 0;
            queryTaskList();
          }
        });
    }
  });
};
const form = reactive({
  name: "",
  metrology: "",
  gid: "",
  price_in: 0,
  price_out: 0,
  price_vip: 0,
  sid: router.currentRoute.value.query.id,
  date: "",
  profit: 0,
  shelfLife: "",
  num: 0,
  type: "",
});
const vipNo = ref("");
const alipayP = () => {
  // caclSum();
  const loading = ElLoading.service({
    lock: true,
    text: "订单创建中（请勿刷新或关闭界面）",
    background: "rgba(0, 0, 0, 0.7)",
  });
  nextTick(() => {
    // Loading should be closed asynchronously
    api
      .post("/pay/aliPay", {
        price: sumM.value.toFixed(2),
        shopName: router.currentRoute.value.query.id,
        userID: userPayID.value,
      })
      .then((res) => {
        if (res.data.msg == "10000") {
          loading.close();
          ElNotification({
            title: "支付成功",
            message: "用户支付成功",
            type: "success",
            duration: 5000,
          });
          dialogVisiblezfb.value = false;
          buy(2, res.data.data.remoteID, res.data.data.shopID);
          userPayID.value = "";
        } else if (res.data.msg == "10003") {
          loading.setText(
            "订单创建成功！等待用户付款中！30秒内未支付则自动取消订单（请勿刷新或关闭界面）"
          );
          var cnt = 0;
          var st = setInterval(function () {
            cnt++;
            api
              .get("/pay/queryAliPay", {
                params: {
                  tradeNo: res.data.data.remoteID,
                },
              })
              .then((res) => {
                if (res.data.data === "TRADE_SUCCESS") {
                  clearInterval(st);
                  loading.close();
                  ElNotification({
                    title: "支付成功",
                    message: "用户支付成功！",
                    type: "success",
                    duration: 5000,
                  });
                  dialogVisiblezfb.value = false;
                  buy(2, res.data.data.remoteID, res.data.data.shopID);
                  userPayID.value = "";
                }
              });
            if (cnt === 10) {
              clearInterval(st);
              api.get("/pay/closePay", {
                params: {
                  tradeNo: res.data.data.remoteID,
                  sid: router.currentRoute.value.query.id,
                },
              });
              api.post("/Goods/buyGood", {
                goods: Trade.get,
                type: 2,
                sum: sumM.value,
                sid: router.currentRoute.value.query.id,
                remoteNo: res.data.data.remoteID,
                status: 2,
                id: res.data.data.shopID,
                vip: isVip.value,
                phone: vipNo.value,
              });
              loading.close();
              ElNotification({
                title: "支付失败",
                message: "用户支付超时！请重新扫描用户付款码",
                type: "error",
                duration: 0,
              });
              userPayID.value = "";
            }
          }, 3000);
        } else {
          api
            .get("/pay/cancelPay", {
              params: {
                tradeNo: res.data.data.remoteID,
                sid: router.currentRoute.value.query.id,
              },
            })
            .then((res) => {
              utils.showMessage(res.data.code, res.data.msg);
            });
          api.post("/Goods/buyGood", {
            goods: Trade.get,
            type: 2,
            sum: sumM.value,
            sid: router.currentRoute.value.query.id,
            remoteNo: res.data.data.remoteID,
            status: 7,
            id: res.data.data.shopID,
            vip: isVip.value,
            phone: vipNo.value,
          });
          loading.close();
          ElNotification({
            title: "支付失败",
            message: "用户支付失败！请重新扫描用户付款码",
            type: "error",
            duration: 0,
          });
          userPayID.value = "";
        }
      });
  });
};
const getSummaries = (param) => {
  const { columns, data } = param;
  const len = columns.length;
  const sums = [];
  columns.forEach((column, index) => {
    if (index === 0) {
      sums[index] = "总计";
    } else if (index === len - 1) {
      const values = data.map((item) =>
        Number((isVip.value === 1 ? item.priceVip : item.priceOut) * item.num)
      );
      if (!values.every((value) => isNaN(value))) {
        sums[index] = values.reduce((prev, curr) => {
          const value = Number(curr);
          if (!isNaN(value)) {
            return prev + curr;
          } else {
            return prev;
          }
        }, 0);
        sums[index] = sums[index].toFixed(2);
        sumM.value = Number(sums[index]);
      } else {
        sums[index] = "N/A";
      }
      //如果是除了第一列和最后一列的其他列，则显示为空
    } else {
      sums[index] = "";
    }
  });
  return sums;
};
const formref = ref();
const rules = reactive({
  name: [
    { required: true, message: "请输入商品名", trigger: "blur" },
    {
      min: 1,
      max: 100,
      message: "请输入商品名",
      trigger: "blur",
    },
  ],
  gid: [
    { required: true, message: "请输入商品条码" },
    {
      min: 1,
      max: 100,
      message: "请输入商品条码",
    },
  ],
  price_out: [
    {
      required: true,
      message: "请输入商品售价",
      trigger: "blur",
    },
    {
      validator: (rule, value, callback) => {
        if (!isNumber(value)) {
          callback(new Error("请输入数字值"));
        } else {
          callback();
        }
      },
      trigger: "blur",
    },
  ],
  price_vip: [
    {
      required: true,
      message: "请输入商品售价",
      trigger: "blur",
    },
    {
      validator: (rule, value, callback) => {
        if (!isNumber(value)) {
          callback(new Error("请输入数字值"));
        } else {
          callback();
        }
      },
      trigger: "blur",
    },
  ],
  shelfLife: [
    { required: true, message: "请输入商品保质期天数", trigger: "blur" },
    { type: "number", message: "保质期必须是数字" },
  ],
  num: [
    { required: true, message: "请输入商品数量", trigger: "blur" },
    { type: "number", message: "商品数量必须是数字" },
  ],
  price_in: [
    {
      required: true,
      message: "请输入商品进价",
      trigger: "blur",
    },
    {
      validator: (rule, value, callback) => {
        if (!isNumber(value)) {
          callback(new Error("请输入数字值"));
        } else {
          callback();
        }
      },
      trigger: "blur",
    },
  ],
});
const isNumber = (val) => {
  var regPos = /^\d+(\.\d+)?$/;
  var regNeg =
    /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/;
  if (regPos.test(val) || regNeg.test(val)) {
    return true;
  } else {
    return false;
  }
};
const drawer = ref(false);
const drawer2 = ref(false);
const tips = ref("");
const hangclick = () => {
  let dt = new Date();
  let y = dt.getFullYear();
  let mt = (dt.getMonth() + 1).toString().padStart(2, "0");
  let day = dt.getDate().toString().padStart(2, "0");
  let h = dt.getHours().toString().padStart(2, "0");
  let m = dt.getMinutes().toString().padStart(2, "0");
  let nowtime = y + "-" + mt + "-" + day + " " + h + ":" + m;
  api.get("/Goods/getRandID").then((res) => {
    if (res.data.code === 200) {
      Hang.add({
        id: res.data.msg,
        time: nowtime,
        tip: tips.value,
        list: Trade.get,
        sum: sumM.value,
      });
      tips.value = "";
      dialogVisiblegd.value = false;
      utils.showMessage(200, "创建挂单成功！");
    } else {
      utils.showMessage(400, "创建挂单失败！请重试");
    }
  });
};
const deleHang = (param) => {
  Hang.del(param);
  utils.showMessage(200, "删除挂单成功！");
};
const dialogVisiblegd = ref(false);
const getHangon = (param) => {
  Trade.set(
    Hang.get.filter((e) => {
      return e.id === param;
    })[0].list
  );
  Hang.del(param);
  drawer.value = false;
  utils.showMessage(200, "取单成功！");
};
let trdeList = ref([]);
const leastFun = () => {
  api
    .get("/trade/queryLeast", {
      params: {
        sid: router.currentRoute.value.query.id,
      },
    })
    .then((res) => {
      trdeList.value = res.data.data;
    });
  drawer2.value = true;
};
let tradetype = ["现金支付", "支付宝支付", "微信支付"];
let tradetype1 = [
  { msg: "等待付款", color: "#fffb09" },
  { msg: "失败", color: "#f60303" },
  { msg: "取消", color: "#f60303" },
  { msg: "完成可退款", color: "#409EFF" },
  { msg: "部分退款", color: "#3febfa" },
  { msg: "全额退款", color: "#ee03f6" },
  { msg: "完成不可退款", color: "#00ff14" },
  { msg: "未知错误交易停止", color: "#f60303" },
];
const getStyle = (data) => {
  return (
    "background-color: " +
    tradetype1[data].color +
    ";\n" +
    "width: 15px;\n" +
    "height: 15px;\n" +
    "border-radius: 50%;\n" +
    "display: block;"
  );
};
const status = ref([]);
onBeforeMount(() => {
  api
    .get("shop/getCategory", {
      params: {
        sid: router.currentRoute.value.query.id,
      },
    })
    .then((res) => {
      options.value = res.data.data;
    });
  api
    .get("shop/getTradeStatus", {
      params: {
        sid: router.currentRoute.value.query.id,
      },
    })
    .then((res) => {
      status.value = res.data.data;
    });
});
</script>

<style scoped lang="scss">
.input-with-select {
  width: 400px;
  margin: 0 !important;
}

.aside {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  border-style: solid;
}

.el-main {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  border-style: solid;
}

.el-footer {
  border: 1px var(--el-border-color);
  border-radius: 6px;
  border-style: solid;
}

.scr1 {
  height: calc(100vh - 130px);
}
</style>
