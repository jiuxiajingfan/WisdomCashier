<template>
  <el-row>
    <el-col :span="3">
      <el-radio-group v-model="isCollapse" style="margin: 0">
        <el-radio-button :label="false">订单数</el-radio-button>
        <el-radio-button :label="true">交易额</el-radio-button>
      </el-radio-group>
    </el-col>
    <el-col :span="3">
      <el-radio-group v-model="isCollapse2" style="margin: 0">
        <el-radio-button :label="false">日统计</el-radio-button>
        <el-radio-button :label="true">月统计</el-radio-button>
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
        :default-time="date"
        @change="refuse"
      />
    </el-col>
  </el-row>
  <el-row style="height: calc(30vh)">
    <el-col :span="12"><div id="container1" /></el-col>
    <el-col :span="12"><div id="container2" /></el-col>
  </el-row>
  <div id="container3" style="height: calc(50vh)"></div>
</template>
<script setup>
import { onMounted, ref, watch } from "vue";
import { Area, Bar, Line, Pie } from "@antv/g2plot";
import api from "@/api/api";
import router from "@/router";
const type = ref("monthrange");
let date = ref([]);
const isCollapse = ref(true);
const isCollapse2 = ref(true);
let area = null;
onMounted(() => {
  let dt = new Date();
  let y = dt.getFullYear();
  let mt = (dt.getMonth() + 1).toString().padStart(2, "0");
  let day = dt.getDate().toString().padStart(2, "0");
  let h = dt.getHours().toString().padStart(2, "0");
  let m = dt.getMinutes().toString().padStart(2, "0");
  let nowtime = y + "-" + mt + "-" + day;
  date.value = [nowtime, nowtime];
  c1();
  api
    .post("/trade/currentTradeMoney", {
      sid: router.currentRoute.value.query.id,
      timeStart: date.value[0] + " 00:00:00",
      timeEnd: date.value[1] + " 23:59:59",
      // timeEnd: "2023-03-29 23:59:59",
      // timeStart: "2023-03-01 00:00:00",
      timeType: isCollapse.value == false ? 1 : 0,
      type: 1,
    })
    .then((res) => {
      c3(res.data.data);
    });
  c2();
});
const c1 = () => {
  const data = [
    { type: "分类一", value: 27 },
    { type: "分类二", value: 25 },
    { type: "分类三", value: 18 },
    { type: "分类四", value: 15 },
    { type: "分类五", value: 10 },
    { type: "其他", value: 5 },
  ];

  const piePlot = new Pie("container1", {
    appendPadding: 10,
    data,
    angleField: "value",
    colorField: "type",
    height: 300,
    width: 300,
    radius: 1,
    innerRadius: 0.64,
    meta: {
      value: {
        formatter: (v) => `¥ ${v}`,
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
const c2 = () => {
  const data = [
    { type: "分类一", value: 27 },
    { type: "分类二", value: 25 },
    { type: "分类三", value: 18 },
    { type: "分类四", value: 15 },
    { type: "分类五", value: 10 },
    { type: "其他", value: 5 },
  ];

  const piePlot = new Pie("container2", {
    appendPadding: 10,
    data,
    angleField: "value",
    colorField: "type",
    height: 300,
    width: 300,
    radius: 1,
    innerRadius: 0.6,
    label: {
      type: "inner",
      offset: "-50%",
      content: "{value}",
      style: {
        textAlign: "center",
        fontSize: 14,
      },
    },
    interactions: [{ type: "element-selected" }, { type: "element-active" }],
    statistic: {
      title: false,
      content: {
        style: {
          whiteSpace: "pre-wrap",
          overflow: "hidden",
          textOverflow: "ellipsis",
        },
        content: "AntV\nG2Plot",
      },
    },
  });

  piePlot.render();
};
const c3 = (res) => {
  const data = res;
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
  api
    .post("/trade/currentTradeMoney", {
      sid: router.currentRoute.value.query.id,
      timeStart: date.value[0] + " 00:00:00",
      timeEnd: date.value[1] + " 23:59:59",
      // timeEnd: "2023-03-29 23:59:59",
      // timeStart: "2023-03-01 00:00:00",
      timeType: isCollapse.value == false ? 1 : 0,
      type: 1,
    })
    .then((res) => {
      area.changeData(res.data.data);
    });
};
watch(isCollapse2, (n, o) => {
  if (o === false) {
    type.value = "monthrange";
  } else {
    type.value = "daterange";
  }
});
</script>
<style lang="scss"></style>
