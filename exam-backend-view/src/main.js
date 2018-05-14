import Vue from 'vue'
import App from './App'
import ElementUI from 'element-ui'
import VueRouter from 'vue-router'
import store from './vuex/store'
import Vuex from 'vuex'
import routes from './routes'
import 'element-ui/lib/theme-default/index.css'
import 'font-awesome/css/font-awesome.min.css'
import './styles/style.less'
import $ from './common/js/jquery-vendor';
import _ from './common/js/lodash-vendor';
import global_ from './config/base.vue';
import promise from 'es6-promise';
import 'babel-polyfill'
import 'event-source-polyfill'
promise.polyfill();

Vue.use(ElementUI)
Vue.use(VueRouter)
Vue.use(Vuex)

Vue.prototype.GLOBAL = global_;

//注册一个全局前置守卫
const router = new VueRouter({
  routes
})

router.beforeEach((to, from, next) => {
    let user = sessionStorage.name;
    let type = sessionStorage.type;
    if(to.path === '/register'){
        next()
    }else if (!user  && to.path !== '/login') {
        alert("请登录！");
        next({ path: '/login' })
    } else {
        next()
    }
});

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

