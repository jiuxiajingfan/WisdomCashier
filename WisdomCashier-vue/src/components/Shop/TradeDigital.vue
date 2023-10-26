<template>
  <el-row>
    <el-col :span="3">
      <el-radio-group v-model="isCollapse" style="margin: 0" @change="refuse">
        <el-radio-button :label="false">订单数</el-radio-button>
        <el-radio-button :label="true">交易额</el-radio-button>
      </el-radio-group>
    </el-col>
    <el-col :span="3">
      <el-radio-group v-model="isCollapse2" style="margin: 0" @change="refuse">
        <el-radio-button :label="false">月统计</el-radio-button>
        <el-radio-button :label="true">日统计</el-radio-button>
      </el-radio-group>
    </el-col>
    <el-col :span="10">
      <el-date-picker
        v-model="date"
        :type="type"
        range-separator="至"
        start-placeholder="起始时间"
        end-placeholder="结束时间"
        value-format="YYYY-MM-DD"
        @change="refuse"
        :disabled-date="disabledDate"
      />
    </el-col>
  </el-row>
  <el-row style="height: calc(30vh)">
    <el-col :span="12">
      <h2>总收入</h2>
      <div id="container1" />
    </el-col>
    <el-col :span="12">
      <h2>收入分类</h2>
      <div id="container2"
    /></el-col>
    <el-col :span="24">
      <h2 v-show="isCollapse === true" style="font-size: 30px">收入变化</h2>
      <h2 v-show="isCollapse === false" style="font-size: 30px">订单变化</h2>
    </el-col>
  </el-row>
  <el-row style="margin-top: 8%">
    <el-col :span="24">
      <div id="container3" style="height: calc(40vh)"></div>
    </el-col>
  </el-row>
</template>
<script setup>
import { onMounted, ref, watch } from "vue";
import { Area, Bar, Line, Pie } from "@antv/g2plot";
import api from "@/api/api";
import router from "@/router";
import utils from "@/utils/utils";
const type = ref("daterange");
let date = ref([]);
const isCollapse = ref(true);
const isCollapse2 = ref(true);
let area = null;
let piePlot = null;
let piePlot2 = null;
const disabledDate = (time) => {
  return time.getTime() > Date.now();
};
let timeType = 0;
let typetype = 0;
onMounted(() => {
  let dt = new Date();
  let y = dt.getFullYear();
  let mt = (dt.getMonth() + 1).toString().padStart(2, "0");
  let day = dt.getDate().toString().padStart(2, "0");
  let h = dt.getHours().toString().padStart(2, "0");
  let m = dt.getMinutes().toString().padStart(2, "0");
  let nowtime = y + "-" + mt + "-" + day;
  let nowtime2 = y + "-" + mt + "-" + "01";
  date.value = [nowtime2, nowtime];
  api
    .post("/trade/currentTradeMoney", {
      sid: router.currentRoute.value.query.id,
      timeStart: date.value[0] + " 00:00:00",
      timeEnd: date.value[1] + " 23:59:59",
      // timeEnd: "2023-03-29 23:59:59",
      // timeStart: "2023-03-01 00:00:00",
      timeType: timeType,
      type: typetype,
    })
    .then((res) => {
      c3(res.data.data[0]);
      c1(res.data.data[1]);
      c2(res.data.data[2]);
    });
});
const c1 = (res) => {
  const data = [];
  for (let i = 0; i < res.length; i++) {
    data.push({
      name: res[i].name,
      value: parseFloat(res[i].value),
    });
  }
  piePlot = new Pie("container1", {
    padding: [5, 5, 5, 5],
    data,
    angleField: "value",
    colorField: "name",
    height: 300,
    width: 300,
    radius: 1,
    innerRadius: 0.64,
    meta: {
      value: {
        formatter: (v) => `¥ ${v.toFixed(2)}`,
      },
    },
    label: {
      type: "inner",
      offset: "-50%",
      autoRotate: false,
      style: { textAlign: "center" },
      formatter: ({ percent }) => `${(percent * 100).toFixed(0)}%`,
    },
    statistic: {
      title: {
        offsetY: -8,
      },
      content: {
        offsetY: -4,
      },
    },
    // 添加 中心统计文本 交互
    interactions: [
      { type: "element-selected" },
      { type: "element-active" },
      {
        type: "pie-statistic-active",
        cfg: {
          start: [
            { trigger: "element:mouseenter", action: "pie-statistic:change" },
            {
              trigger: "legend-item:mouseenter",
              action: "pie-statistic:change",
            },
          ],
          end: [
            { trigger: "element:mouseleave", action: "pie-statistic:reset" },
            {
              trigger: "legend-item:mouseleave",
              action: "pie-statistic:reset",
            },
          ],
        },
      },
    ],
  });

  piePlot.render();
};
const c2 = (res) => {
  const data = [];
  for (let i = 0; i < res.length; i++) {
    data.push({
      name: res[i].name,
      value: parseFloat(res[i].value),
    });
  }
  piePlot2 = new Pie("container2", {
    padding: [5, 5, 5, 5],
    data,
    angleField: "value",
    colorField: "name",
    height: 300,
    width: 300,
    radius: 1,
    label: {
      type: "inner",
      offset: "-30%",
      content: ({ percent }) => `${(percent * 100).toFixed(0)}%`,
      style: {
        fontSize: 14,
        textAlign: "center",
      },
    },
    interactions: [{ type: "element-active" }],
  });

  piePlot.render();
  piePlot2.render();
};
const c3 = (res) => {
  const data = [];
  res.forEach((e) => {
    data.push({
      name: e.name,
      value: parseFloat(e.value),
    });
  });
  area = new Area("container3", {
    data,
    xField: "name",
    yField: "value",
    xAxis: {
      range: [0, 1],
      tickCount: 5,
    },
    padding: [30, 100, 20, 100],
    areaStyle: () => {
      return {
        fill: "l(270) 0:#7ec2f3 1:#1890ff",
      };
    },
  });
  area.render();
};
const refuse = () => {
  if (isCollapse2.value === false) {
    timeType = 1;
  } else {
    timeType = 0;
  }
  if (isCollapse.value === false) {
    typetype = 1;
  } else {
    typetype = 0;
  }
  api
    .post("/trade/currentTradeMoney", {
      sid: router.currentRoute.value.query.id,
      timeStart: date.value[0] + " 00:00:00",
      timeEnd: date.value[1] + " 23:59:59",
      // timeEnd: "2023-03-29 23:59:59",
      // timeStart: "2023-03-01 00:00:00",
      timeType: timeType,
      type: typetype,
    })
    .then((res) => {
      if (res.data.code === 200) {
        const da3 = [];
        res.data.data[0].forEach((e) => {
          da3.push({
            name: e.name,
            value: parseFloat(e.value),
          });
        });
        area.changeData(da3);
        const da = [];
        res.data.data[1].forEach((e) => {
          da.push({
            name: e.name,
            value: parseFloat(e.value),
          });
        });
        piePlot.changeData(da);
        const da2 = [];
        res.data.data[2].forEach((e) => {
          da2.push({
            name: e.name,
            value: parseFloat(e.value),
          });
        });
        piePlot2.changeData(da2);
      } else {
        utils.showErrMessage(res.data.msg);
      }
    });
};
watch(isCollapse2, (n, o) => {
  if (o === true) {
    type.value = "monthrange";
  } else {
    type.value = "daterange";
  }
});
</script>
<style lang="scss"></style>
