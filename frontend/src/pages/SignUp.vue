<template>
  <div class='content'>
    <div class='center'>
      <h2>SIGN UP</h2>
      <h4>Kickstart Your Business Today!</h4>
      <p v-if='errors.length'>
      <ul class='error_list'>
        <li v-for='error in errors' v-bind:key='error' class='errors'> {{ error }}</li>
      </ul>
      </p>
      <div class='flex-form'>
        <div class='half'>
          <input class="form_input firstname" v-model='firstName' id='firstname' type='text' placeholder='First Name'>
          <input class="form_input lastname" v-model='lastName' id='lastname' type='text' placeholder='Last Name'>
        </div>
        <input class="form_input" v-model='email' type='email' placeholder='Email' id='email'>
        <input class="form_input" v-model='password' type='password' placeholder='Password' id='password'>
        <input class="form_input" v-model='cpassword' type='password' placeholder='Confirm Password' id='cpassword'>
        <button class='button' @click='this.signUp'>Sign Up</button>
      </div>
    </div>
  </div>
</template>
<script>
  import axios from 'axios'

  export default {
    data() {
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
        if (!this.cpassword) {
          this.errors.push('Please confirm your password.')
        } else if (!this.passwordConfirmed(this.password, this.cpassword)) {
          this.errors.push('Passwords do not match. Please confirm again.')
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
      passwordConfirmed: function (p, cp) {
        if (p === cp) {
          return true
        } else {
          return false
        }
      },
      signUp: function (e) {
        if (!this.isValid(e)) {
          console.log('invalid form')
        } else {
          axios.post('/api/owner', {
            firstName: this.firstName,
            lastName: this.lastName,
            email: this.email,
            password: this.password
          })
            .then((response) => {
              console.log(response.status)
              this.$router.push({path: '/login'})
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

  .half {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;
  }

  .form_input {
    margin: 10px 4px;
    padding: 10px;
    border-radius: 10px;
    border: #FFFFFF;
    box-shadow: 0 0 4px rgba(0, 0, 0, 0.25);
  }

  .firstname {
    flex: 1;
  }

  .lastname {
    flex: 1;
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
    padding: 4px 0px;
    list-style-type: none;
    border: 2px solid darkred;
    border-radius: 5px;
    font-size: 12px;
    width: 100%
  }
</style>
