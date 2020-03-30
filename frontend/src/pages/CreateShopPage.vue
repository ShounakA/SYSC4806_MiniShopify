<template>
  <div class='content'>
    <div>
      <h2><b>CREATE A SHOP</b></h2>
      <div class='flex-form'>
        <h2>SHOP NAME:</h2>
        <input class='form_input' type='text' v-model='name' placeholder='Enter shop name...' id='shop_name'><br>
        <h2>SHOP DESCRIPTION:</h2>
        <input class='form_input' type='text' v-model='description' placeholder='Enter shop description...'
               id='shop_description'><br>
        <h2>SHOP TAGS:</h2>
        <input class='form_input' type='text' v-model='tags'
               placeholder='Enter shop tags (separate tags with a comma)...' id='shop_tags'><br>
        <div>
          <component v-for='product in products' v-bind:key='product' v-bind:is='product'/>
          <button class='add_button' v-on:click='addNewItem'>+</button>
        </div>
        <button class='button' v-on:click='createShop'>Create Shop</button>
      </div>
    </div>
  </div>
</template>

<script scoped>
import axios from 'axios'
import EditItem from '../components/EditItem'
import {
  getCookie,
  OWNER_ID_HEADER_STRING,
  TOKEN_COOKIE_HEADER,
  TOKEN_PREFIX
} from '../constants/constants'
export default {
  data () {
    return {
      response: [],
      errors: [],
      name: '',
      description: '',
      tags: '',
      userId: 0,
      products: []
    }
  },
  name: 'CreateShopPage',
  components: {
    EditItem
  },
  methods: {
    addNewItem () {
      this.products.push(EditItem)
    },
    getTags (input) {
      // get individual tags
      var tempTags = input.split(',')
      var tags = []
      tempTags.forEach(function (tag) {
        tags.push(tag.trim())
      })
      return tags
    },
    createShop () {
      if (this.checkInputFields()) {
        var token = JSON.parse(getCookie(TOKEN_COOKIE_HEADER))
        console.log(token)
        this.userId = token[OWNER_ID_HEADER_STRING]
        var tags = this.getTags(this.tags)
        axios.post('/api/shop', {
          name: this.name,
          desc: this.description,
          tags: tags,
          userId: this.userId,
          products: this.products
        }, {headers: {Authorization: TOKEN_PREFIX + token[TOKEN_COOKIE_HEADER]}})
          .then((response) => {
            // alert('Shop was created!')//REMOVE THIS LATER
            // console.log(response)
            this.$router.push({path: '/'})
          })
          .catch((error) => {
            console.log(error)
          })
      } else {
        alert('Input fields were not valid')
        console.log('Input fields were not valid')
      }
    },
    checkInputFields () {
      if (this.name !== '' && this.description !== '' && this.tags !== '' && this.products.length > 0) {
        var tags = this.tags.split(',')
        tags.forEach(function (tag) {
          if (tag.trim() === '') {
            return false
          }
        })
        this.products.forEach(function (product) {
          if (product.name === '' || product.description === '' || product.stockQuantity > -1) {
            alert('Product information not valid')
            console.log('Product information not valid')
            return false
          }
        })
      } else {
        return false
      }
      return true
    }
  }
}
</script>

<style>
  .content {
    margin: 2% 15% 0% 15%;
    background: #DDECFF;
    box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
    border-radius: 49px;
    padding-top: 5%;
    padding-bottom: 5%;
    text-align: center;
  }

  .flex-form {
    display: flex;
    flex-direction: column;
    flex-wrap: wrap;
    justify-content: center;
  }

  .form_input {
    margin: 10px 5% 10px 5%;
    padding: 10px;
    border-radius: 10px;
  }

  .add_button {
    width: 50%;
    margin: 10px;
    padding: 10px;
    background-color: #A3CBFF;
    border-radius: 10px;
  }

  .button {
    margin: 10px 5% 10px 5%;
    padding: 10px;
    border-radius: 10px;
  }
</style>
