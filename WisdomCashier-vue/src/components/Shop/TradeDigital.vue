<template>
  <div id="container" style="height: 400px"></div>
</template>
<script>
import { Chart } from "@antv/g2";
export default {
  mounted() {
    // 数据源
    const data = [
      { time: "8/1", value: 240 },
      { time: "8/10", value: 600 },
      { time: "8/20", value: 350 },
      { time: "8/31", value: 470 },
    ];
    // 初始化图表，列出图表属性
    const chart = new Chart({
      container: "container",
      autoFit: true,
      padding: [100, 20, 30, 40],
    });
    // 图表关联数据chart.position()方法决定的，在下面有设置chart.position("time*value")
    // 前面为x轴，所以 time 为 x 轴， value 为 y 轴
    chart.data(data);
    // 度量
    // x，y轴坐标系是根据
    chart.scale({
      // y轴坐标系设置
      value: {
        min: 0,
        nice: true,
        alias: "用户",
      },
      // x轴坐标系设置
      time: {
        range: [0, 1],
      },
    });
    // 提示信息
    chart.tooltip({
      // 是否显示辅助线
      showCrosshairs: true,
      // 是否合并所有点展示
      shared: false,
    });
    // value 坐标系设置
    chart.axis("value", {
      grid: null,
      tickLine: null,
    });
    // time 坐标系设置
    chart.axis("time", {
      line: null,
      tickLine: null,
    });
    // 图表绘制设置
    // 面积图
    chart
      .area()
      .position("time*value")
      .color("l(90) 0:#0b83de 1:#0c1c2d")
      .shape("smooth");
    // 折线图
    chart.line().position("time*value").color("#00d0f6").shape("smooth");
    // 渲染图表
    chart.render();
  },
};
</script>
