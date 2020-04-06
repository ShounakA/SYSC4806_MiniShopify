// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import VueRouter from 'vue-router'
import Dashboard from './pages/Dashboard'
import SignUp from './pages/SignUp'
import Error from './pages/Error'
import Login from './pages/Login'
import ShopPage from './pages/ShopPage'
import AllShopsPage from './pages/AllShopsPage';
import CreateShopPage from "./pages/CreateShopPage";
import Search from './pages/Search'
import CartPage from "./pages/CartPage";
Vue.config.productionTip = false
Vue.use(VueRouter)

const routes = [
  {path: '/', component: Dashboard},
  {path: '/signup', name:'signup', component: SignUp},
  {path: '/cart', name:'cart', component: CartPage},
  {path: '/error', name:'error', component: Error},
  {path: '/login', name:'login', component: Login},
  {path: '/shop', name: 'shop', component: ShopPage, props: (route) => ({shopId: route.query.shopId})},
  {path: '/create', name: 'create', component: CreateShopPage},
  {path: '/edit', name: 'edit', component: CreateShopPage, props: (route) => ({shopId: route.query.shopId})},
  {path: '/all_shops', name: 'all_shops', component: AllShopsPage},
  {path: '/search', component: Search}
]

const router = new VueRouter({
  routes
})
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: {App},
  template: '<App/>'
})
