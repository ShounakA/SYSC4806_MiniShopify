<template>
  <div class='content'>
    <div class='center'>
      <icon></icon><h1>Mini-Shopify</h1>

      <div class='flex-form'>
        <div class='instr'>
          <h4>SIGN UP</h4>
          <h4>Kickstart Your Business Today!</h4>
        </div>
        <p v-if='errors.length'>
          <ul>
            <li v-for='error in errors' v-bind:key='error' class='errors'> {{ error }} </li>
          </ul>
        </p>
        <div class='half'>
          <input v-model='firstName' class='signupform' id='firstname' type='text' placeholder='First Name'>
          <input v-model='lastName' class='signupform' id='lastname' type='text' placeholder='Last Name'>
        </div>
        <input v-model='email' class='signupform' type='email' placeholder='Email' id='email'><br>
        <input v-model='password' class='signupform' type='password' placeholder='Password' id='password'><br>
        <input v-model='cpassword' class='signupform' type='password' placeholder='Confirm Password' id='cpassword'><br>
        <button @click='this.signUp'>Sign Up</button>

      </div>
    </div>
  </div>
</template>
<script>
import axios from 'axios'
export default {
  data () {
    return {
      response: [],
      errors: [],
      firstName: '',
      lastName: '',
      email: '',
      password: '',
      cpassword: ''
    }
  },
  methods: {
    isValid: function (e) {
      this.errors = []
      if (!this.firstName) {
        this.errors.push('First Name required.')
      }
      if (!this.lastName) {
        this.errors.push('Last Name required.')
      }
      if (!this.firstName) {
        this.errors.push('First Name required.')
      }
      if (!this.email) {
        this.errors.push('Email required.')
      } else if (!this.validEmail(this.email)) {
        this.errors.push('Valid email required.')
      }
      if (!this.password) {
        this.errors.push('Password required.')
      }
      if (!this.cpassword) { this.errors.push('Please confirm your password.') }
      else if (!this.passwordConfirmed(this.password, this.cpassword)) {
        this.errors.push('Passwords do not match. Please confirm again.')
      }
      if (this.errors.length === 0) { return true }
      else { return false }
    },
    validEmail: function (email) {
      var re = /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/
      return re.test(email)
    },
    passwordConfirmed: function (p, cp) {
      if (p === cp) { return true }
      else { return false }
    },
    signUp: function (e) {
      if (!this.isValid(e)) { console.log('invalid form') }
      else {
        axios.post('/api/owner', {
          firstName: this.firstName,
          lastName: this.lastName,
          email: this.email,
          password: this.password
        })
          .then((response) => {
            console.log(response)
            this.$router.push({path: '/'})
          })
          .catch((error) => {
            console.log(error)
          })
      }
    }
  }
}
</script>
<style scoped>
  .content{
    margin: auto;
    margin-top: 2%;
    width: 50%;
    background: #DDECFF;
    box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
    border-radius: 49px;
    padding-top: 5%;
    padding-bottom: 5%;
    text-align: center;
  }
  .center{
    margin:auto;
    width:50%;
  }
  .flex-form{
    display:flex;
    flex-direction: column;
    flex-wrap: wrap;
    justify-content: center;
  }
  #firstname{
    width:50%;
  }
  #lastname{
    width: 50%;
  }
  .half{
    display: flex;
    flex-direction: row;
    align-items: center;
  }
  .instr{
    text-align: left;
  }
  .errors{
    list-style-type: none;
    border: 2px solid darkred;
    border-radius: 5px;
    font-size: 12px;
    width:100%
  }
</style>
