<template>
  <div id='app'>
    <div id='navbar'>
      <div>
        <icon></icon><span id='title'> Mini-Shopify </span>
      </div>
      <div class='links'>
        <span><a @click='this.toggleLogin'>{{linkTextLogin}}</a></span>
        <router-link to='/signup'><button id='signup'>Sign Up</button></router-link>
      </div>
    </div>
    <router-view @userLogin='this.updateBar'></router-view>
  </div>
</template>

<script>
import {TOKEN_COOKIE_HEADER, setCookie} from './constants/constants'
export default {
  name: 'App',
  data () {
    return {
      isUserLogged: false,
      linkTextLogin: 'Login'
    }
  },
  methods: {
    updateBar: function (e) {
      this.isUserLogged = true
      this.linkTextLogin = 'Logout'
    },
    logoutUser: function () {
      // deleting token
      setCookie(TOKEN_COOKIE_HEADER, '', 0)
      this.isUserLogged = false
      this.linkTextLogin = 'Login'
    },
    toggleLogin: function () {
      if (this.isUserLogged) { this.logoutUser() }
      else {
        this.$router.push({path: '/login'})
      }
    }
  }
}
</script>

<style scoped>
  #navbar {
    display: flex;
    margin-left: 15%;
    margin-right:15%;
    padding-left: 2%;
    padding-right: 5%;
    background: #DDECFF;
    box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
    border-radius: 30px;
    justify-content: space-between;
  }
  #title {
    font-family: Nunito;
    font-style: normal;
    font-weight: bold;
    font-size: 36px;
    line-height: 49px;
  }
  .links{
    display: flex;
    line-height: 49px;
    width: 200px;
    justify-content: space-between;
  }
  #signup{
    padding-left: 10%;
    padding-right: 10%;
    background: #007DC4;
    border-radius: 8px;
    box-shadow: 2px 4px 4px rgba(0, 0, 0, 0.1);
    color: white;
  }
  .errors{
    list-style-type: none;
    border: 2px solid darkred;
    border-radius: 5px;
    font-size: 12px;
  }
</style>
