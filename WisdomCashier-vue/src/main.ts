import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";
import pinia from "@/store/store";
import * as ElementPlusIconVue from "@element-plus/icons-vue";
const app = createApp(App);
app.use(ElementPlus);
for (const [key, component] of Object.entries(ElementPlusIconVue)) {
  app.component(key, component);
}
app.use(router);
pinia.use(piniaPluginPersistedstate);
app.use(pinia);
app.mount("#app");
