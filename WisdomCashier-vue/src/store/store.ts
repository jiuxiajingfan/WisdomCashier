import { createPinia } from "pinia";
import piniaPersist from "pinia-plugin-persistedstate";
const pinia = createPinia();
pinia.use(piniaPersist);
export default pinia;
