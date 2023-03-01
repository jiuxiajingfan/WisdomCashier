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
            <el-table :data="data2">
              <el-table-column label="名称" prop="name"></el-table-column>
              <el-table-column label="编号" prop="gid"></el-table-column>
              <el-table-column label="数量" prop="num"></el-table-column>
              <el-table-column label="单价" prop="price_out"></el-table-column>
              <el-table-column label="总价">
                <template v-slot="scope">
                  {{ scope.row.num * scope.row.price_out }}
                </template>
              </el-table-column>
            </el-table>
          </el-scrollbar>
        </el-aside>
        <el-container>
          <el-main>
            <div v-if="data2.length != 0">
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
                <div>
                  <h2>
                    {{ lastData.metrology }}
                  </h2>
                </div>
                <div style="margin-top: 20px">
                  <label>数量:</label>
                  <el-input-number
                    style="margin-left: 20px"
                    v-model="lastData.num"
                    :min="1"
                    :max="100"
                  />
                </div>
                <div style="margin-top: 20px">
                  <label>价格:</label>
                  <el-input-number
                    style="margin-left: 20px"
                    v-model="lastData.num"
                    :min="1"
                    :max="100"
                    :precision="2"
                  />
                </div>
              </div>
            </div>
            <el-empty v-else image-size="400" description="暂无商品" />
          </el-main>
          <el-footer>Footer</el-footer>
        </el-container>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import api from "@/api/api";
import { useRouter } from "vue-router/dist/vue-router";
let searchText = ref("");
const data2 = ref([]);
const router = useRouter();
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
  name: "777777777",
  num: 1,
  price_out: "",
  picUrl: "",
  metrology: "",
  gid: "",
});
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
          searchText.value = "";
          lastData.num = 1;
          lastData.name = res.data.data.name;
          lastData.metrology = res.data.data.metrology;
          lastData.picUrl = res.data.data.picUrl;
          lastData.price_out = res.data.data.priceOut;
          lastData.gid = searchText.value;
          data2.value = [];
          for (let key of dataMap.keys()) {
            data2.value.push(dataMap.get(key));
            console.log(data2);
          }
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
    searchText.value = "";
    lastData.num = newVar.num + 1;
    lastData.name = newVar.name;
    lastData.metrology = newVar.metrology;
    lastData.picUrl = newVar.picUrl;
    lastData.price_out = newVar.price_out;
    lastData.gid = searchText.value;
    data2.value = [];
    for (let key of dataMap.keys()) {
      data2.value.push(dataMap.get(key));
      console.log(data2);
    }
  }
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
