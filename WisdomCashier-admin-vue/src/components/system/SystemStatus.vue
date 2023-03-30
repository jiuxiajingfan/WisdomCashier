<template>
  <el-row style="margin-top: 30px">
    <el-col :span="12">
      <el-card>
        <div id="c1"></div>
      </el-card>
    </el-col>
    <el-col :span="12">
      <el-card>
        <div id="c2"></div>
      </el-card>
    </el-col>
  </el-row>
  <el-card style="margin-top: 5%">
    <template #header>
      <div class="card-header">
        <h1>系统信息</h1>
      </div>
    </template>
    <el-descriptions direction="horizontal" column="1" size="default" border>
      <el-descriptions-item label="CPU信息" label-align="center" align="center">
        <pre>{{ info.cpu }}</pre>
      </el-descriptions-item>
      <el-descriptions-item
        label="CPU核心数"
        label-align="center"
        align="center"
      >
        {{ info.cpus }}
      </el-descriptions-item>
      <el-descriptions-item
        label="内存总量"
        label-align="center"
        align="center"
      >
        {{ info.mem }}
      </el-descriptions-item>
    </el-descriptions>
  </el-card>
</template>

<script setup>
import { onMounted, onUnmounted, reactive } from "vue";
import { Liquid, measureTextWidth } from "@antv/g2plot";
import api from "@/api/api";
const info = reactive({
  cpu: "",
  cpuS: "",
  mem: "",
});
let p1 = null;
let p2 = null;
onUnmounted(() => {
  timer && clearInterval(timer);
});
onMounted(() => {
  api.get("account/getSystem").then((res) => {
    info.cpu = res.data.data[0][2].value;
    console.log(res.data.data[0][2].value);
    info.cpus = res.data.data[0][1].value;
    info.mem = res.data.data[0][0].value;
    c1(res.data.data[1][1].value);
    c2(res.data.data[1][0].value);
  });
});
const c1 = (res) => {
  p1 = new Liquid(document.getElementById("c1"), {
    percent: parseFloat(res),
    radius: 1.0,
    height: 150,
    width: 150,
    statistic: {
      title: {
        formatter: () => "Cpu使用率",
        style: ({ percent }) => ({
          fill: percent > 0.65 ? "white" : "rgba(44,53,66,0.85)",
        }),
      },
      content: {
        style: ({ percent }) => ({
          fontSize: 50,
          lineHeight: 1,
          fill: percent > 0.65 ? "white" : "rgba(44,53,66,0.85)",
        }),
        customHtml: (container, view, { percent }) => {
          const { width, height } = container.getBoundingClientRect();
          const d = Math.sqrt(Math.pow(width / 2, 2) + Math.pow(height / 2, 2));
          const text = ` ${(percent * 100).toFixed(0)}%`;
          const textWidth = measureTextWidth(text, { fontSize: 60 });
          const scale = Math.min(d / textWidth, 1);
          return `<div style="width:${d}px;display:flex;align-items:center;justify-content:center;font-size:${scale}em;line-height:${
            scale <= 1 ? 1 : "inherit"
          }">${text}</div>`;
        },
      },
    },
    liquidStyle: ({ percent }) => {
      return {
        fill: percent > 0.8 ? "#f80404" : "#5B8FF9",
        stroke: percent > 0.8 ? "#f80404" : "#5B8FF9",
      };
    },
    color: () => "#5B8FF9",
  });
  p1.render();
};
const c2 = (res) => {
  p2 = new Liquid(document.getElementById("c2"), {
    percent: parseFloat(res),
    radius: 1.0,
    height: 150,
    width: 150,
    statistic: {
      title: {
        formatter: () => "内存使用率",
        style: ({ percent }) => ({
          fill: percent > 0.65 ? "white" : "rgba(44,53,66,0.85)",
        }),
      },
      content: {
        style: ({ percent }) => ({
          fontSize: 50,
          lineHeight: 1,
          fill: percent > 0.65 ? "white" : "rgba(44,53,66,0.85)",
        }),
        customHtml: (container, view, { percent }) => {
          const { width, height } = container.getBoundingClientRect();
          const d = Math.sqrt(Math.pow(width / 2, 2) + Math.pow(height / 2, 2));
          const text = ` ${(percent * 100).toFixed(0)}%`;
          const textWidth = measureTextWidth(text, { fontSize: 60 });
          const scale = Math.min(d / textWidth, 1);
          return `<div style="width:${d}px;display:flex;align-items:center;justify-content:center;font-size:${scale}em;line-height:${
            scale <= 1 ? 1 : "inherit"
          }">${text}</div>`;
        },
      },
    },
    liquidStyle: ({ percent }) => {
      return {
        fill: percent > 0.8 ? "#f80404" : "#5B8FF9",
        stroke: percent > 0.8 ? "#f80404" : "#5B8FF9",
      };
    },
    color: () => "#5B8FF9",
  });
  p2.render();
};
let timer = null;
timer = setInterval(() => {
  api.get("account/getSystem").then((res) => {
    p1.changeData(parseFloat(res.data.data[1][1].value));
    p2.changeData(parseFloat(res.data.data[1][0].value));
  });
}, 10000);
</script>

<style scoped></style>
