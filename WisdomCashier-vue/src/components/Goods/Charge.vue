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
                <el-button icon="Search" @click="queryTaskList">搜索</el-button>
              </template>
            </el-input>
            <el-table :data="data2" @row-click="onRowClick" show-summary>
              <el-table-column label="名称" prop="name"></el-table-column>
              <el-table-column label="编号" prop="gid"></el-table-column>
              <el-table-column label="数量" prop="num"></el-table-column>
              <el-table-column label="单价" prop="price_out"></el-table-column>
              <el-table-column label="总价">
                <template v-slot="scope">
                  {{
                    math
                      .multiply(
                        math.bignumber(scope.row.num),
                        math.bignumber(scope.row.price_out)
                      )
                      .toFixed(2)
                  }}
                </template>
              </el-table-column>
            </el-table>
          </el-scrollbar>
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
                    v-model="lastData.price_out"
                    :min="1"
                    :max="100"
                    :precision="2"
                    :disabled="lastData.price_out != ''"
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
            <el-button size="large" style="height: 80%; margin-top: 5px">
              <save
                theme="outline"
                size="28"
                fill="#333"
                :strokeWidth="1"
                style="margin-right: 10px"
              />
              挂单</el-button
            >
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
                  <el-button> 离开 </el-button>
                  <el-button type="primary">添加</el-button>
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
import { reactive, ref } from "vue";
import api from "@/api/api";
import { useRouter } from "vue-router/dist/vue-router";
import * as math from "mathjs";
import utils from "@/utils/utils";
import { Wechat, Alipay, PaperMoney, Save } from "@icon-park/vue-next";
let searchText = ref("");
const data2 = ref([]);
const router = useRouter();
let picshow = ref(false);
let data = reactive({
  name: "",
  num: 1,
  price_out: "",
  picUrl: "",
  metrology: "",
  gid: "",
});
let dataMap = reactive(new Map());
let lastData = reactive({
  name: "",
  num: 1,
  price_out: "",
  picUrl: "",
  metrology: "",
  gid: "",
});
const cle = () => {
  searchText.value = "";
  data2.value = [];
  for (let key of dataMap.keys()) {
    data2.value.push(dataMap.get(key));
    console.log(data2);
  }
};
const queryTaskList = () => {
  var newVar = dataMap.get(searchText.value);
  if (newVar === undefined) {
    console.log("第一次查");
    api
      .get("/Goods/getGood", {
        params: {
          gid: searchText.value,
          sid: router.currentRoute.value.query.id,
        },
      })
      .then((res) => {
        if (res.data.code === 200) {
          data.num = 1;
          data.name = res.data.data.name;
          data.metrology = res.data.data.metrology;
          data.picUrl = res.data.data.picUrl;
          data.price_out = res.data.data.priceOut;
          data.gid = searchText.value;
          dataMap.set(searchText.value, {
            num: data.num,
            name: data.name,
            metrology: data.metrology,
            picUrl: data.picUrl,
            price_out: data.price_out,
            gid: data.gid,
          });
          lastData.num = 1;
          lastData.name = res.data.data.name;
          lastData.metrology = res.data.data.metrology;
          lastData.picUrl = res.data.data.picUrl;
          lastData.price_out = res.data.data.priceOut;
          lastData.gid = searchText.value;
          cle();
          picshow.value = true;
        } else {
          console.log("No");
        }
      });
  } else {
    data.num = newVar.num + 1;
    data.name = newVar.name;
    data.metrology = newVar.metrology;
    data.picUrl = newVar.picUrl;
    data.price_out = newVar.price_out;
    data.gid = searchText.value;
    dataMap.set(searchText.value, {
      num: data.num,
      name: data.name,
      metrology: data.metrology,
      picUrl: data.picUrl,
      price_out: data.price_out,
      gid: data.gid,
    });
    data.num = 1;
    lastData.num = newVar.num + 1;
    lastData.name = newVar.name;
    lastData.metrology = newVar.metrology;
    lastData.picUrl = newVar.picUrl;
    lastData.price_out = newVar.price_out;
    lastData.gid = searchText.value;
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
    price_out: lastData.price_out,
    gid: lastData.gid,
  });
  cle();
  picshow.value = false;
  utils.showMessage(200, "修改数量成功！");
};
const onRowClick = (row, column, event) => {
  console.log(row);
  lastData.num = row.num;
  lastData.name = row.name;
  lastData.metrology = row.metrology;
  lastData.picUrl = row.picUrl;
  lastData.price_out = row.price_out;
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
const onMonery = () => {
  sumM.value = 0;
  for (let i = 0; i < data2.value.length; i++) {
    sumM.value += math.multiply(data2.value[i].num, data2.value[i].price_out);
  }
  moneyCharge.value = true;
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
