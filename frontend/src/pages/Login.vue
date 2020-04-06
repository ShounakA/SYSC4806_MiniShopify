<template>
  <div class='content'>
    <div class='center'>
      <icon></icon>
      <h2>LOGIN</h2>
      <h4>Kickstart Your Business Today!</h4>
      <p v-if='errors.length'>
      <ul class='error_list'>
        <li v-for='error in errors' v-bind:key='error' class='errors'> {{ error }}</li>
      </ul>
      </p>
      <div class='flex-form'>
        <input class="form_input" v-model='email' type='email' placeholder='Email'>
        <input class="form_input" v-model='password' type='password' placeholder='Password'>
        <button class='button' @click='this.login'>Login</button>
      </div>
    </div>
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
    data() {
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
        if (this.errors.length === 0) {
          return true
        } else {
          return false
        }
      },
      validEmail: function (email) {
        var re = /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/
        return re.test(email)
      },
      login: function (e) {
        if (!this.isValid(e)) {
          console.log('invalid form')
        } else {
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
              this.$router.push({path: '/'})
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
  .content {
    margin: 16px 15% 0 15%;
    background: #DDECFF;
    box-shadow: 0 0 4px rgba(0, 0, 0, 0.25);
    border-radius: 49px;
    padding-top: 5%;
    padding-bottom: 5%;
    text-align: center;
  }

  .center {
    margin: auto;
    width: 50%;
  }

  .flex-form {
    display: flex;
    flex-direction: column;
    flex-wrap: wrap;
    justify-content: center;
  }

  .form_input {
    margin: 10px 4px;
    padding: 10px;
    border-radius: 10px;
    border: #FFFFFF;
    box-shadow: 0 0 4px rgba(0, 0, 0, 0.25);
  }

  .button {
    margin: 10px 4px;
    padding: 10px;
    background-color: #f0f0f0;
    border: #f0f0f0;
    border-radius: 10px;
    box-shadow: 0 0 4px rgba(0, 0, 0, 0.25);
  }

  .error_list {
    padding-inline-start: 0px;
  }

  .errors {
    list-style-type: none;
    border: 2px solid darkred;
    border-radius: 5px;
    font-size: 12px;
    width: 100%
  }
</style>
