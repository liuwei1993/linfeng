import App from './App'

// #ifndef VUE3
import Vue from 'vue'
import './uni.promisify.adaptor'

App.mpType = 'app'
const app = new Vue({
	store,
  ...App
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
import uView from './uni_modules/vk-uview-ui';
import store from './store';

export function createApp() {
  const app = createSSRApp(App)
  
  app.use(uView)
  app.use(store)
  
  app.provide('$store', store)
  
  return {
    app
  }
}
// #endif