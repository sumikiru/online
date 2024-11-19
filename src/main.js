import './assets/main.css'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
const app = createApp(App)
app.use(ElementPlus, { size: 'small', zIndex: 3000 })

import { createApp } from 'vue'
import App from './App.vue'

createApp(App).mount('#app')
