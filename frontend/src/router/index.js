import Vue from 'vue'
import VueRouter from 'vue-router'
import Dashboard from '../pages/Dashboard.vue'
import SignUp from "../pages/SignUp";
import CartPage from "../pages/CartPage";
import Login from "../pages/Login";
import Error from "../pages/Error";
import ShopPage from "../pages/ShopPage";
import CreateShopPage from "../pages/CreateShopPage";
import AllShopsPage from "../pages/AllShopsPage";
import Search from "../pages/Search";
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
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
