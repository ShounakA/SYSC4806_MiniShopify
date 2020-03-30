<template>
  <div class='content'>
    <icon></icon><h1>Mini-Shopify</h1>
    <h4>LOGIN</h4>
    <h4>Kickstart Your Business Today!</h4>
    <p v-if='errors.length'>
    <ul>
      <li v-for='error in errors' v-bind:key='error' class='errors'> {{ error }} </li>
    </ul>
    </p>
    <input v-model='email' class='loginform' type='email' placeholder='Email'><br>
    <input v-model='password' class='loginform' type='password' placeholder='Password'><br>
    <button @click='this.login'>Login</button>
  </div>
</template>
<script>
import axios from 'axios'
import {
  TOKEN_COOKIE_HEADER,
  TOKEN_HEADER_STRING,
  TOKEN_PREFIX,
  OWNER_ID_HEADER_STRING,
  setCookie
} from '../constants/constants'
export default {
  data () {
    return {
      response: [],
      errors: [],
      email: '',
      password: ''
    }
  },
  methods: {
    isValid: function (e) {
      this.errors = []
      if (!this.email) {
        this.errors.push('Email required.')
      } else if (!this.validEmail(this.email)) {
        this.errors.push('Valid email required.')
      }
      if (!this.password) {
        this.errors.push('Password required.')
      }
      if (this.errors.length === 0) { return true }
      else { return false }
    },
    validEmail: function (email) {
      var re = /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/
      return re.test(email)
    },
    login: function (e) {
      if (!this.isValid(e)) { console.log('invalid form') }
      else {
        axios.post('/api/login', {
          email: this.email,
          password: this.password
        })
          .then((response) => {
            var mytoken = response.headers[TOKEN_HEADER_STRING]
            const ownerId = response.headers[OWNER_ID_HEADER_STRING]
            console.log(response)
            mytoken = mytoken.replace(TOKEN_PREFIX, '')
            const myCookie = {
              token: mytoken,
              ownerid: ownerId
            }
            const strCookie = JSON.stringify(myCookie)
            setCookie(TOKEN_COOKIE_HEADER, strCookie, 10)
            this.$emit('userLogin')
            this.$router.go(-1)
          })
          .catch((error) => {
            console.log(error)
            this.errors.push('Invalid username or password.')
          })
      }
    }
  }
}
</script>
<style scoped>
  .errors{
    list-style-type: none;
    border: 2px solid darkred;
    border-radius: 5px;
    font-size: 12px;
    width:100%
  }
</style>
