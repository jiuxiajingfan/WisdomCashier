<template>
  <dev class="searchBox" style="width: 100%; text-align: center">
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
    <el-button
      style="margin-left: 20px"
      icon="Plus"
      type="primary"
      @click="openadd"
      >新增商品
    </el-button>
  </dev>
  <div class="table">
    <el-table :data="good" height="calc(100vh - 230px)">
      <el-table-column prop="pic" label="图片">
        <template v-slot="scope">
          <el-upload
            class="avatar-uploader"
            action="up"
            :show-file-list="false"
            accept=".jpg,.jpeg,.JPG,.JPEG"
            data="multipartFile"
            :auto-upload="false"
          >
            <el-icon
              v-if="scope.row.picUrl == null"
              class="avatar-uploader-icon"
            >
              <Plus />
            </el-icon>
            <el-image
              v-else-if="scope.row.picUrl != null"
              style="width: 100px; height: 100px"
              :src="scope.row.picUrl"
              fit="fit"
            />
          </el-upload>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="商品名" width="auto" />
      <el-table-column prop="metrology" label="单位" width="auto" />
      <el-table-column
        prop="priceIn"
        label="进价"
        width="auto"
        :formatter="rounding"
      />
      <el-table-column
        prop="priceOut"
        label="售价"
        width="auto"
        :formatter="rounding"
      />
      <el-table-column
        prop="profit"
        label="利润"
        width="auto"
        :formatter="rounding"
      />
      <el-table-column prop="num" label="数量" width="auto" />
      <el-table-column prop="deadline" label="过期时间" width="auto" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button
            link
            type="primary"
            size="large"
            @click="updateGood(scope.row)"
          >
            更新
          </el-button>
          <el-popconfirm
            title="确定要删除吗?"
            @confirm="deleteGood(scope.row.gid)"
          >
            <template #reference>
              <el-button link type="primary">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :hide-on-single-page="false"
      layout="->, total, prev, pager, next, sizes"
      v-model:page-size="pageSize"
      :page-sizes="[10, 20, 50, 100]"
      background
      :total="total"
      :pager-count="7"
      @current-change="taskCurrentChange"
      @size-change="taskSizeChange"
    >
    </el-pagination>
  </div>
  <el-dialog v-model="dialogFormVisible" title="新增商品" width="900px" center>
    <el-form
      :model="form"
      ref="formref"
      :inline="true"
      label-position="left"
      style="margin-left: 10%"
      :rules="rules"
    >
      <div>
        <el-form-item label="商品名" :label-width="formLabelWidth" prop="name">
          <el-input style="width: 190px" v-model="form.name" />
        </el-form-item>
        <el-form-item label="商品条码" :label-width="formLabelWidth" prop="gid">
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
      </div>
      <div>
        <el-form-item label="数量" :label-width="formLabelWidth" prop="num">
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
          <el-input style="width: 190px" v-model.number="form.shelfLife" />
        </el-form-item>
      </div>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">离开</el-button>
        <el-button type="primary" @click="save"> 保存 </el-button>
      </span>
    </template>
  </el-dialog>
  <el-dialog v-model="dialogFormVisible3" title="更新商品" width="900px" center>
    <el-form
      :model="form"
      ref="formref"
      :inline="true"
      label-position="left"
      style="margin-left: 10%"
      :rules="rules"
    >
      <div>
        <el-form-item label="商品名" :label-width="formLabelWidth" prop="name">
          <el-input style="width: 190px" v-model="form.name" />
        </el-form-item>
        <el-form-item label="商品条码" :label-width="formLabelWidth" prop="gid">
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
      </div>
      <div>
        <el-form-item label="数量" :label-width="formLabelWidth" prop="num">
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
          <el-input style="width: 190px" v-model.number="form.shelfLife" />
        </el-form-item>
      </div>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible3 = false">离开</el-button>
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
</template>

<script setup>
import { onBeforeMount, reactive, ref } from "vue";
import api from "@/api/api";
import { useRouter } from "vue-router/dist/vue-router";
import Utils from "@/utils/utils";
import utils from "@/utils/utils";

let dialogFormVisible = ref(false);
const dialogFormVisible2 = ref(false);
const dialogFormVisible3 = ref(false);
const formLabelWidth = "140px";
let good = ref([]);
let current = ref(1);
let total = ref(0);
let pageSize = ref(20);
let searchText = ref("");
let currentText = "";
let sid = ref(1);
var reg = /^[0-9]*$/;
const router = useRouter();
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
const formref = ref();
const save = () => {
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
            dialogFormVisible.value = false;
            del();
            searchText.value = "";
            queryTaskList();
          }
        });
    }
  });
};
const save2 = () => {
  formref.value.validate((valid) => {
    if (valid) {
      api
        .post("/Goods/updateGood", {
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
            dialogFormVisible.value = false;
            del();
            searchText.value = "";
            queryTaskList();
          }
        });
    }
  });
};
const del = () => {
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
const leave = () => {
  dialogFormVisible2.value = false;
  del();
};
onBeforeMount(() => {
  queryTaskList();
});
const queryTaskList = () => {
  sid.value = router.currentRoute.value.query.id;
  if (currentText != searchText.value) {
    current.value = 1;
    currentText = searchText.value;
  }
  api
    .post("Goods/getGoodPage", {
      current: current.value,
      pageSize: pageSize.value,
      gid: searchText.value,
      sid: router.currentRoute.value.query.id,
    })
    .then((res) => {
      good.value = res.data.data.records;
      current.value = res.data.data.current;
      total.value = res.data.data.total;
      if (good.value.length === 0) dialogFormVisible2.value = true;
    });
};
const taskCurrentChange = (cnt) => {
  current.value = cnt;
  queryTaskList();
};
const taskSizeChange = (ps) => {
  pageSize.value = ps;
  queryTaskList();
};
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
const rounding = (row, column) => {
  return parseFloat(row[column.property]).toFixed(2);
};
const updateGood = (row) => {
  form.sid = row.sid;
  form.gid = row.gid;
  form.num = row.num;
  form.name = row.name;
  form.metrology = row.metrology;
  form.shelfLife = row.shelfLife;
  form.price_out = row.priceOut;
  form.price_in = row.priceIn;
  dialogFormVisible3.value = true;
};
const deleteGood = (row) => {
  api
    .get("/Goods/deleteGood", {
      params: {
        sid: router.currentRoute.value.query.id,
        gid: row,
      },
    })
    .then((res) => {
      utils.showMessage(res.data.code, res.data.msg);
      queryTaskList();
    });
};
</script>

<style scoped lang="scss">
.searchBox {
  .el-input {
    width: 60%;
  }
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 90px;
  height: 90px;
  text-align: center;
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
}
</style>
