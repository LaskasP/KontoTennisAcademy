import "./assets/main.css";

import { createApp } from "vue";
import { createPinia } from "pinia";

import App from "./App.vue";
import router from "./router";

import BaseCard from "@/components/base/BaseCard.vue";
import BaseButton from "@/components/base/BaseButton.vue";

const app = createApp(App);

app.use(createPinia());
app.use(router);

app.component("base-card", BaseCard);
app.component("base-button", BaseButton);

app.mount("#app");
