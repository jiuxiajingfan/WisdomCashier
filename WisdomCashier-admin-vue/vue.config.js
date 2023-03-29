module.exports = {
  devServer: {
    proxy: {
      "/api": {
        // 此处的写法，目的是为了 将 /api 替换成 https://www.baidu.com/
        target: "http://localhost:8087/admin",
        // 允许跨域
        changeOrigin: true,
        ws: true,
        pathRewrite: {
          "^/api": "http://localhost:8087",
        },
      },
    },
  },
};
