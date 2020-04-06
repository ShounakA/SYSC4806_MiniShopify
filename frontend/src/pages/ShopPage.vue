<template>
  <div class='content'>
    <div class='flex-form'>
      <div class="shop_info_container">
        <h2>Shop Name:</h2>
        <h2 id='shop_name'>{{name}}</h2>
      </div>
      <div>
        <h2>Shop Description:</h2>
        <h2 id='shop_description'>{{description}}</h2>
      </div>
      <div>
        <h2>Shop Tags:</h2>
        <h2 id='shop_tags'>{{tags}}</h2>
      </div>
      <div class="product_item_container">
        <ProductItem v-for='(product, index) in products' v-bind:key='product.id' v-bind:id='product.id'
                     v-bind:name="product.name"
                     v-bind:description="product.description" v-bind:stockPrice="product.price"
                     v-bind:stockQuantity="product.stockQuantity"/>
      </div>
    </div>
  </div>
</template>

<script>
  import axios from 'axios'
  import ProductItem from '../components/ProductItem'
  import {getCookie, OWNER_ID_HEADER_STRING, TOKEN_COOKIE_HEADER, TOKEN_PREFIX,} from '../constants/constants'

  export default {
    name: 'ShopPage',
    components: {
      ProductItem,
    },
    props: {
      shopId: {
        type: String,
        default: null
      }
    },
    mounted() {
      if (getCookie(TOKEN_COOKIE_HEADER) !== '') {
        let token = JSON.parse(getCookie(TOKEN_COOKIE_HEADER));
        this.userId = token[OWNER_ID_HEADER_STRING];
      }
      this.getShop();
    },
    data() {
      return {
        errors: [],
        name: '',
        description: '',
        tags: '',
        userId: null,
        ownerId: null,
        products: []
      }
    },
    methods: {
      getShop() {
        if (this.shopId != null) {
          axios.get('/api/public/shop', {params: {shopId: this.shopId}})
            .then((response) => {
              console.log(response.status);
              this.name = response.data.name;
              this.description = response.data.description;
              this.tags = this.formatTags(response.data.tags);
              this.ownerId = response.data.owner.id;
              this.products = response.data.products;
            })
            .catch((error) => {
              console.log(error)
            });
        } else {
          console.log("shopId was null");
        }
      },
      formatTags: (tagsArray) => {
        let tags = '';
        tagsArray.forEach((tag) => {
          tags += '#' + tag + ' ';
        });
        return tags;
      },
      addItemToCart(itemId, itemQuantity) {
        let token = JSON.parse(getCookie(TOKEN_COOKIE_HEADER));
        console.log("OwnerID: ", this.ownerId)
        axios.post('/api/cart/add', {
          shopId: this.shopId,
          id: itemId,
          stock: itemQuantity,
        }, {params: {ownerId: this.userId}, headers: {Authorization: TOKEN_PREFIX + token[TOKEN_COOKIE_HEADER]}})
          .then((response) => {
            console.log(response.status);
            this.getShop();
          })
          .catch((error) => {
            console.log(error)
          })
      },
    }
  }
</script>

<style scoped>
  .content {
    margin: 16px 15% 0 15%;
    background: #DDECFF;
    box-shadow: 0 0 4px rgba(0, 0, 0, 0.25);
    border-radius: 49px;
    padding-top: 16px;
    padding-bottom: 16px;
    text-align: center;
  }

  .shop_info_container {
    display: flex;
    flex-direction: row;
    justify-content: center;
  }

  h2 {
    margin: 4px;
  }

  .flex-form {
    display: flex;
    flex-direction: column;
    justify-content: center;
  }

  .form_input {
    margin: 10px 5% 10px 5%;
    padding: 10px;
    border-radius: 10px;
    border: #FFFFFF;
    box-shadow: 0 0 4px rgba(0, 0, 0, 0.25);
  }
</style>
