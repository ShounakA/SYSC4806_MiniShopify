<template>
  <div id='app'>
    <div id='navbar'>
      <div>
        <router-link to='/' tag='span' id="title"><a class="dashboard_link">Mini-Shopify</a></router-link>
      </div>
      <div class='links'>
        <span class="login_button"><a @click='toggleLogin'>{{linkTextLogin}}</a></span>
        <select v-if="isUserLogged" class="signup_button" @change="handleChange">
          <option value="" selected disabled hidden>More</option>
          <option value='cart'>Cart</option>
          <option value='all_shops'>All Shops</option>
          <option value='create'>Add Shop</option>
          <option value='search'>Search</option>
        </select>
        <router-link v-if="!isUserLogged" class='signup_button' to='/signup'
                     tag='button'>Sign Up
        </router-link>
      </div>
    </div>
    <router-view @userLogin='this.updateBar'></router-view>
  </div>
</template>

<script>
  import {TOKEN_COOKIE_HEADER, setCookie} from './constants/constants'

  export default {
    name: 'App',
    data() {
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
        this.$router.push({path: '/'})
      },
      toggleLogin: function () {
        if (this.isUserLogged) {
          this.logoutUser()
        } else {
          this.$router.push({path: '/login'})
        }
      },
      handleChange: function (e) {
        let val = e.target.value
        console.log(val)
        this.$router.push({path: '/' + val})
      }
    }
  }
</script>

<style>
  @import url('https://fonts.googleapis.com/css?family=Nunito&display=swap');

  * {
    font-family: Nunito;
  }

  a:hover {
    cursor: pointer;
  }

  button:hover {
    cursor: pointer;
  }

  #navbar {
    display: flex;
    margin-left: 15%;
    margin-right: 15%;
    padding-left: 2%;
    padding-right: 5%;
    background: #DDECFF;
    box-shadow: 0 0 4px rgba(0, 0, 0, 0.25);
    border-radius: 30px;
    justify-content: space-between;
  }

  .dashboard_link {
    color: #000000;
  }

  option {
    text-align: center;
  }

  #title {
    font-style: normal;
    font-weight: bold;
    font-size: 36px;
    line-height: 49px;
  }

  .links {
    display: flex;
    line-height: 49px;
    width: 200px;
    align-items: center;
    justify-content: space-between;
  }

  input, select, option {
    padding: 12px 20px;
    margin: 8px 4px;
    box-sizing: border-box;
    border-radius: 10px;
  }

  .login_button {
    margin: auto;
  }

  .signup_button {
    color: white;
    padding: 8px 12px;
    margin: auto;
    background: #007DC4;
    border: #007DC4;
    border-radius: 8px;
    box-shadow: 0 0 4px rgba(0, 0, 0, 0.1);
  }
</style>
