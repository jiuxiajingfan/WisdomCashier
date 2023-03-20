<template>
  <el-input
    placeholder="请输入要退款的订单号"
    v-model="searchText"
    class="input-with-select"
    @keyup.enter="queryTaskList"
    style="width: 60%"
  >
    <template #append>
      <el-button icon="Search" @click="queryTaskList">搜索</el-button>
    </template>
  </el-input>
  <h2 style="font-size: 20px; margin-top: 50px">订单详情</h2>
  <el-table :data="trade" height="calc(50vh - 230px)" v-loading="lod">
    <el-table-column type="expand">
      <div m="4">
        <el-table :data="tradeDetail">
          <el-table-column label="名称" prop="name"></el-table-column>
          <el-table-column label="编号" prop="gid"></el-table-column>
          <el-table-column label="数量" prop="num"></el-table-column>
          <el-table-column label="单价" prop="priceOut"></el-table-column>
        </el-table>
      </div>
    </el-table-column>
    <el-table-column prop="id" label="流水号" width="auto" />
    <el-table-column prop="income" label="金额" width="auto" />
    <el-table-column label="交易状态" width="auto">
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
              {{ tradetype2[scope.row.status].msg }}
            </span>
          </div>
        </el-tooltip>
      </template>
    </el-table-column>
    <el-table-column label="付款方式" width="auto">
      <template #default="scope">
        <span style="margin-left: 2px">
          {{ tradetype[scope.row.type - 1] }}
        </span>
      </template>
    </el-table-column>
    <el-table-column prop="createTime" label="交易时间" width="auto" />
    <el-table-column prop="operater" label="交易员" width="auto" />
  </el-table>
  <h2 style="font-size: 20px; margin-top: 50px">退款详情</h2>
  <el-table :data="tradeRefund" height="calc(50vh - 230px)" v-loading="lod">
    <el-table-column prop="no" label="退款流水号" width="auto" />
    <el-table-column prop="money" label="退款金额" width="auto" />
    <el-table-column label="退款状态" width="auto">
      <template #default="scope">
        <el-tooltip
          class="box-item"
          effect="dark"
          :content="scope.row.errMsg"
          placement="top"
        >
          <div style="display: flex; align-items: center">
            <i :style="getStyle2(scope.row.status)"></i>
            <span style="margin-left: 2px">
              {{ tradetype3[scope.row.status].msg }}
            </span>
          </div>
        </el-tooltip>
      </template>
    </el-table-column>
    <el-table-column label="退款方式" width="auto">
      <template #default="scope">
        <span style="margin-left: 2px">
          {{ tradetype4[scope.row.type - 1] }}
        </span>
      </template>
    </el-table-column>
    <el-table-column prop="ctrateTime" label="退款时间" width="auto" />
    <el-table-column prop="msg" label="退款原因" width="auto" />
    <el-table-column prop="operater" label="操作员" width="auto" />
  </el-table>
  <div style="margin-top: 20px">
    <el-button
      size="large"
      style="margin-left: 30px; margin-top: 5px; height: 80%"
      @click="centerDialogVisible7 = true"
      :disabled="flag"
    >
      <paper-money
        theme="outline"
        size="28"
        fill="#333"
        :strokeWidth="1"
        style="margin-right: 10px"
      />
      现金退款</el-button
    >
    <el-button
      size="large"
      style="margin-left: 30px; margin-top: 5px; height: 80%"
      @click="centerDialogVisible = true"
      :disabled="type !== 2 || flag"
    >
      <alipay
        theme="outline"
        size="28"
        fill="#333"
        :strokeWidth="1"
        style="margin-right: 10px"
      />
      支付宝退款</el-button
    >
    <el-button
      size="large"
      style="margin-left: 30px; margin-top: 5px; height: 80%"
      :disabled="type !== 3"
    >
      <wechat
        theme="outline"
        size="28"
        fill="#333"
        :strokeWidth="1"
        style="margin-right: 10px"
      />
      微信退款</el-button
    >
  </div>
  <el-dialog
    v-model="centerDialogVisible"
    title="发起退款"
    width="30%"
    align-center
  >
    <span
      >该订单已累计退款： {{ tradeRefund.length }} 次，剩余可退款金额为：{{
        (sumM - cut).toFixed(2)
      }}
    </span>
    <h2 style="font-size: 20px">请确认后输入退款金额及原因</h2>
    <el-input v-model="mon" placeholder="请输入退款金额"></el-input>
    <el-input v-model="msg" placeholder="请输入退款原因"></el-input>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="sure" :loading="lod">
          确定
        </el-button>
      </span>
    </template>
  </el-dialog>
  <el-dialog
    v-model="centerDialogVisible7"
    title="发起退款"
    width="30%"
    align-center
  >
    <span
      >该订单已累计退款： {{ tradeRefund.length }} 次，剩余可退款金额为：{{
        (sumM - cut).toFixed(2)
      }}
    </span>
    <h2 style="font-size: 20px">请确认后输入退款金额及原因</h2>
    <el-input v-model="mon" placeholder="请输入退款金额"></el-input>
    <el-input v-model="msg" placeholder="请输入退款原因"></el-input>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="centerDialogVisible7 = false">取消</el-button>
        <el-button type="primary" @click="sure2" :loading="lod">
          确定
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from "vue";
import api from "@/api/api";
import router from "@/router";
import { Wechat, Alipay, PaperMoney } from "@icon-park/vue-next";
import utils from "@/utils/utils";
import { ElNotification } from "element-plus";
let trade = ref([]);
let tradeDetail = ref([]);
let tradeRefund = ref([]);
let searchText = ref("");
let mon = ref();
let msg = ref("");
let centerDialogVisible7 = ref(false);
const fluse = () => {
  api
    .get("/trade/queryGoodsById", {
      params: {
        id: searchText.value,
      },
    })
    .then((res) => {
      if (res.data.code === 200) {
        tradeDetail.value = res.data.data;
      } else {
        utils.showErrMessage(res.data.msg);
      }
    });
};
const sure = () => {
  api
    .post("pay/refundPay", {
      sid: router.currentRoute.value.query.id,
      money: mon.value,
      tradeNo: searchText.value,
      msg: msg.value,
      no: searchText.value + no,
    })
    .then((res) => {
      if (res.data.code === 200) {
        ElNotification({
          title: "退款成功",
          type: "success",
          duration: 5000,
        });
        refund();
      } else {
        ElNotification({
          title: "退款失败",
          message: res.data.msg,
          type: "error",
          duration: 5000,
        });
        refund();
      }
    })
    .finally(() => {
      lod.value = false;
      mon.value = 0;
      msg.value = "";
      centerDialogVisible7.value = false;
    });
};
const lod = ref(false);
const sure2 = () => {
  lod.value = true;
  api
    .post("trade/cashTradeRefund", {
      sid: router.currentRoute.value.query.id,
      money: mon.value,
      tradeNo: searchText.value,
      msg: msg.value,
      no: searchText.value + no,
    })
    .then((res) => {
      if (res.data.code === 200) {
        ElNotification({
          title: "退款成功",
          type: "success",
          duration: 5000,
        });
        refund();
      } else {
        ElNotification({
          title: "退款失败",
          message: res.data.msg,
          type: "error",
          duration: 5000,
        });
        refund();
      }
    })
    .finally(() => {
      lod.value = false;
      mon.value = 0;
      msg.value = "";
      centerDialogVisible7.value = false;
    });
};

const flag = ref(true);
const refund = () => {
  api
    .get("trade/queryRefund", {
      params: {
        id: searchText.value,
        sid: router.currentRoute.value.query.id,
      },
    })
    .then((res) => {
      if (res.data.code === 200) {
        tradeRefund.value = res.data.data;
        if (tradeRefund.value.length > 0) {
          cut.value = 0;
          tradeRefund.value.forEach((e) => {
            if (e.status === 1) {
              cut.value += e.money;
            }
          });
          no = tradeRefund.value.length;
        }
      } else {
        utils.showErrMessage(res.data.msg);
      }
    });
};
const queryTaskList = () => {
  lod.value = true;
  if (searchText.value === "") {
    utils.showMessage(400, "请输入订单号");
    return;
  }
  api
    .post("trade/queryTradePage", {
      id: searchText.value,
      sid: router.currentRoute.value.query.id,
    })
    .then((res) => {
      if (res.data.code === 200) {
        trade.value = res.data.data.records;
        if (trade.value.length !== 0) {
          type.value = res.data.data.records[0].type;
          sumM.value = res.data.data.records[0].income;
          if (
            res.data.data.records[0].status === 3 ||
            res.data.data.records[0].status === 4
          ) {
            flag.value = false;
          }
          refund();
          fluse();
        }
      } else {
        utils.showErrMessage(res.data.msg);
      }
    })
    .finally(() => {
      lod.value = false;
    });
};
const type = ref("0");
const sumM = ref(0);
const cut = ref(Number);
const centerDialogVisible = ref(false);
let tradetype = ["现金支付", "支付宝支付", "微信支付"];
let tradetype4 = ["现金退款", "支付宝退款", "微信退款"];
let tradetype2 = [
  { msg: "等待付款", color: "#fffb09" },
  { msg: "失败", color: "#f60303" },
  { msg: "取消", color: "#f60303" },
  { msg: "完成可退款", color: "#409EFF" },
  { msg: "部分退款", color: "#3febfa" },
  { msg: "全额退款", color: "#ee03f6" },
  { msg: "完成不可退款", color: "#00ff14" },
  { msg: "未知错误交易停止", color: "#f60303" },
];
let tradetype3 = [
  { msg: "失败", color: "#f60303" },
  { msg: "退款成功", color: "#31ff01" },
];
let no = 0;
watch(searchText, (o, n) => {
  no = 0;
  sumM.value = 0;
  cut.value = 0;
});
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
const getStyle2 = (data) => {
  return (
    "background-color: " +
    tradetype3[data].color +
    ";\n" +
    "width: 15px;\n" +
    "height: 15px;\n" +
    "border-radius: 50%;\n" +
    "display: block;"
  );
};
</script>

<style scoped></style>
