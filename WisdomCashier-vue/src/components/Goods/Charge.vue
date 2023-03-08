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
            >
              <template #append>
                <el-button
                  icon="Search"
                  @click="queryTaskList"
                  :disabled="searchText.trim().length === 0"
                  >搜索</el-button
                >
              </template>
            </el-input>
            <el-button style="margin-left: 10px" @click="hangclick"
              >挂单</el-button
            >
            <el-table
              :data="productList"
              @row-click="onRowClick"
              show-summary
              height="calc(100vh - 170px)"
              :summary-method="getSummaries"
            >
              <el-table-column label="名称" prop="name"></el-table-column>
              <el-table-column label="编号" prop="gid"></el-table-column>
              <el-table-column label="数量" prop="num"></el-table-column>
              <el-table-column label="单价" prop="priceOut"></el-table-column>
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
                    @confirm="deleHang(scope.row.time)"
                  >
                    <template #reference>
                      <el-button link type="primary">删除</el-button>
                    </template>
                  </el-popconfirm>
                  <el-button
                    link
                    type="primary"
                    @click="getHangon(scope.row.time)"
                    >取单</el-button
                  >
                </template>
              </el-table-column>
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
                  :src="lastData.picUrl"
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
                    >删除</el-button
                  >
                  <el-button
                    type="primary"
                    size="large"
                    style="margin-left: 20px"
                    @click="sure(lastData.gid)"
                    >确定</el-button
                  >
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
            <el-badge :value="12" class="item">
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
                挂单详情</el-button
              >
            </el-badge>
            <el-button
              size="large"
              style="margin-left: 30px; margin-top: 5px; height: 80%"
              @click="onMonery"
            >
              <paper-money
                theme="outline"
                size="28"
                fill="#333"
                :strokeWidth="1"
                style="margin-right: 10px"
              />
              现金结算</el-button
            >
            <el-button
              size="large"
              style="margin-left: 30px; margin-top: 5px; height: 80%"
              @click="zfbBut"
            >
              <alipay
                theme="outline"
                size="28"
                fill="#333"
                :strokeWidth="1"
                style="margin-right: 10px"
              />
              支付宝结算</el-button
            >
            <el-button
              size="large"
              style="margin-left: 30px; margin-top: 5px; height: 80%"
            >
              <wechat
                theme="outline"
                size="28"
                fill="#333"
                :strokeWidth="1"
                style="margin-right: 10px"
              />
              微信结算</el-button
            >
            <el-dialog
              v-model="moneyCharge"
              title="现金收款"
              :center="true"
              align-center
            >
              <h2>应收款： {{ sumM.toFixed(2) }} 元</h2>
              <div>
                <label style="font-size: 20px">实际付款：</label>
                <el-input
                  style="margin-left: 20px; width: 300px"
                  v-model="giveMoney"
                ></el-input>
              </div>
              <h2>找零：{{ (giveMoney - sumM).toFixed(2) }}</h2>
              <template #footer>
                <span class="dialog-footer">
                  <el-button @click="moneyCharge = false"> 离开 </el-button>
                  <el-button type="primary" @click="buy(1, '')">确认</el-button>
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
                    <el-input
                      style="width: 190px"
                      v-model.number="form.price_out"
                    />
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
              v-model="dialogVisiblezfb"
              title="支付宝支付"
              width="30%"
            >
              <h2 style="font-size: 30px">请扫描或输入顾客付款条形码</h2>
              <el-input v-model="userPayID" @keyup.enter="alipayP"></el-input>
              <template #footer>
                <span class="dialog-footer">
                  <el-button
                    @click="dialogVisiblezfb = false"
                    autofocus="autofocus"
                    >Cancel</el-button
                  >
                  <el-button type="primary" @click="alipayP"> 确定 </el-button>
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
import { nextTick, onMounted, reactive, ref } from "vue";
import api from "@/api/api";
import { useRouter } from "vue-router/dist/vue-router";
import * as math from "mathjs";
import utils from "@/utils/utils";
import { Wechat, Alipay, PaperMoney, Save } from "@icon-park/vue-next";
import Utils from "@/utils/utils";
import { ElLoading, ElNotification } from "element-plus";
import { useGoodStore } from "@/store/goods";
import pinia from "@/store/store";
import { storeToRefs } from "pinia/dist/pinia";
import { useHangStore } from "@/store/hangon";
const good = useGoodStore(pinia);
const Hang = useHangStore(pinia);
const { hangList } = storeToRefs(Hang);
const { productList } = storeToRefs(good);
let searchText = ref("");
const router = useRouter();
let dialogVisiblezfb = ref(false);
let picshow = ref(false);
let data = reactive({
  name: "",
  num: 1,
  priceOut: "",
  picUrl: "",
  metrology: "",
  gid: "",
});
let dataMap = reactive(new Map());
let lastData = reactive({
  name: "",
  num: 1,
  priceOut: "",
  picUrl: "",
  metrology: "",
  gid: "",
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
};
const cle = () => {
  searchText.value = "";
  good.set([]);
  for (let key of dataMap.keys()) {
    good.push(dataMap.get(key));
  }
  good.save();
};
var reg = /^[0-9]*$/;
const openadd2 = () => {
  dialogFormVisible2.value = false;
  openadd();
};
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
const queryTaskList = () => {
  var newVar = dataMap.get(searchText.value.trim());
  if (newVar === undefined) {
    api
      .get("/Goods/getGood", {
        params: {
          gid: searchText.value.trim(),
          sid: router.currentRoute.value.query.id,
        },
      })
      .then((res) => {
        if (res.data.code === 200) {
          data.num = 1;
          data.name = res.data.data.name;
          data.metrology = res.data.data.metrology;
          data.picUrl = res.data.data.picUrl;
          data.priceOut = res.data.data.priceOut;
          data.gid = searchText.value.trim();
          dataMap.set(searchText.value.trim(), {
            num: data.num,
            name: data.name,
            metrology: data.metrology,
            picUrl: data.picUrl,
            priceOut: data.priceOut,
            gid: data.gid,
          });
          lastData.num = 1;
          lastData.name = res.data.data.name;
          lastData.metrology = res.data.data.metrology;
          lastData.picUrl = res.data.data.picUrl;
          lastData.priceOut = res.data.data.priceOut;
          lastData.gid = searchText.value.trim();
          cle();
          picshow.value = true;
        } else {
          dialogFormVisible2.value = true;
        }
      });
  } else {
    data.num = newVar.num + 1;
    data.name = newVar.name;
    data.metrology = newVar.metrology;
    data.picUrl = newVar.picUrl;
    data.priceOut = newVar.priceOut;
    data.gid = searchText.value.trim();
    dataMap.set(searchText.value.trim(), {
      num: data.num,
      name: data.name,
      metrology: data.metrology,
      picUrl: data.picUrl,
      priceOut: data.priceOut,
      gid: data.gid,
    });
    data.num = 1;
    lastData.num = newVar.num + 1;
    lastData.name = newVar.name;
    lastData.metrology = newVar.metrology;
    lastData.picUrl = newVar.picUrl;
    lastData.priceOut = newVar.priceOut;
    lastData.gid = searchText.value.trim();
    cle();
    picshow.value = true;
  }
};
const sure = (gid) => {
  dataMap.set(gid, {
    num: lastData.num,
    name: lastData.name,
    metrology: lastData.metrology,
    picUrl: lastData.picUrl,
    priceOut: lastData.priceOut,
    gid: lastData.gid,
  });
  cle();
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
  lastData.gid = row.gid;
  picshow.value = true;
};
const dele = (gid) => {
  dataMap.delete(gid);
  cle();
  picshow.value = false;
  utils.showMessage(200, "删除商品成功！");
};
const moneyCharge = ref(false);
const giveMoney = ref(0);
let sumM = ref(0);
// const caclSum = () => {
//   sumM.value = 0;
//   for (let i = 0; i < productList.length; i++) {
//     sumM.value += math.multiply(productList[i].num, productList[i].priceOut);
//   }
// };
const onMonery = () => {
  // caclSum();
  moneyCharge.value = true;
};
const buy = (type, no) => {
  api
    .post("/Goods/buyGood", {
      goods: good.get,
      type: type,
      sum: sumM.value,
      sid: router.currentRoute.value.query.id,
      remoteNo: no,
    })
    .then((res) => {
      if (res.data.code === 200) {
        if (type === 1) moneyCharge.value = false;
        utils.showMessage(res.data.code, "结算成功！");
        good.set([]);
        dataMap.clear();
        sumM.value = 0;
        giveMoney.value = 0;
        picshow.value = false;
      } else {
        utils.showMessage(res.data.code, "结算失败请重试或联系管理员！");
      }
    });
};
let dialogFormVisible = ref(false);
const dialogFormVisible2 = ref(false);
const formLabelWidth = "140px";
const save2 = () => {
  formref.value.validate((valid) => {
    if (valid) {
      api
        .post("/Goods/addGood", {
          name: form.name,
          gid: form.gid,
          priceIn: form.price_in,
          priceOut: form.price_out,
          sid: form.sid,
          date: form.date,
          shelfLife: form.shelfLife,
          num: form.num,
          profit: form.price_out - form.price_in,
          metrology: form.metrology,
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
  sid: router.currentRoute.value.query.id,
  date: "",
  profit: 0,
  shelfLife: "",
  num: 0,
});
const zfbBut = () => {
  dialogVisiblezfb.value = true;
};
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
        price: sumM.value,
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
          buy(2, res.data.data.remoteID);
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
                  buy(2, res.data.data.remoteID);
                  userPayID.value = "";
                }
              });
            if (cnt === 10) {
              clearInterval(st);
              api.get("/pay/closePay", {
                params: {
                  tradeNo: res.data.data.remoteID,
                },
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
              },
            })
            .then((res) => {
              utils.showMessage(res.data.code, res.data.msg);
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
      const values = data.map((item) => Number(item.priceOut * item.num));
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
const hangclick = () => {
  let dt = new Date();
  let y = dt.getFullYear();
  let mt = (dt.getMonth() + 1).toString().padStart(2, "0");
  let day = dt.getDate().toString().padStart(2, "0");
  let h = dt.getHours().toString().padStart(2, "0");
  let m = dt.getMinutes().toString().padStart(2, "0");
  let nowtime = y + "-" + mt + "-" + day + " " + h + ":" + m;
  Hang.add({
    time: nowtime,
    tip: "hello",
    list: good.get,
    sum: sumM.value,
  });
};
const deleHang = (param) => {
  debugger;
  Hang.del(param);
  utils.showMessage(200, "删除挂单成功！");
};
const getHangon = (param) => {
  good.set(
    Hang.get.filter((e) => {
      return e.time === param;
    })[0].list
  );
  Hang.del(param);
  drawer.value = false;
  utils.showMessage(200, "取单成功！");
};
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
