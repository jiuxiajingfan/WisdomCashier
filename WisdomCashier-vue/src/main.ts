import { createApp } from "vue";
import App from "./App.vue";
import "./registerServiceWorker";
import router from "./router";
import store from "./store";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import axios from "axios";
import "element-plus/dist/index.css";
import * as ElementPlusIconVue from "@element-plus/icons-vue";
const app = createApp(App);
app.use(ElementPlus);
for (const [key, component] of Object.entries(ElementPlusIconVue)) {
  app.component(key, component);
}
app.use(router);
app.use(store);
app.mount("#app");
