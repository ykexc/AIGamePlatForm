import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from '@/App.vue'
import router from './router'
import 'element-plus/dist/index.css'
import 'element-plus/theme-chalk/display.css'
import axios from "axios";
const app = createApp(App)

app.use(createPinia())
app.use(router)
axios.defaults.baseURL = 'http://127.0.0.1:3000'
app.mount('#app')
