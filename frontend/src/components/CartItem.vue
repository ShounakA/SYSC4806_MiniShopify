<template>
  <div class="content">
    <h2>Name: {{name}}</h2>
    <h3>Description: {{description}}</h3>
    <h3>Price: {{price}}</h3>
    <div class="cart_remove_options">
      <button class='remove_from_cart_button' @click='removeItemFromCart'>Remove From Cart</button>
    </div>
  </div>
</template>

<script>
  import {getCookie, OWNER_ID_HEADER_STRING, TOKEN_COOKIE_HEADER, TOKEN_PREFIX} from "../constants/constants";
  import axios from "axios";

  export default {
    name: "CartItem",
    props: {id: Number, name: String, description: String, quantity: Number, price: Number, shopId: Number},
    methods: {
      removeItemFromCart() {
        let token = JSON.parse(getCookie(TOKEN_COOKIE_HEADER));
        let currentUserId = token[OWNER_ID_HEADER_STRING];
        if (currentUserId != '') {
          axios.post('/api/cart/remove',
            {
              id: this.id,
              shopId: this.shopId
            },
            {
              params: {ownerId: currentUserId},
              headers: {Authorization: TOKEN_PREFIX + token[TOKEN_COOKIE_HEADER]}
            }
          ).then((response) => {
            console.log(response.status);
            this.$parent.getCart();
          }).catch((error) => {
            console.log(error);
          });
        } else {
          console.log("User not logged in");
        }
      }
    },
  }
</script>

<style scoped>
  .content {
    flex: 1;
    cursor: pointer;
    margin: 10px 5%;
    background: #A3CBFF;
    box-shadow: 0 0 4px rgba(0, 0, 0, 0.25);
    border-radius: 49px;
    text-align: center;
  }

  .cart_remove_options {
    display: flex;
    flex-direction: row;
    justify-content: center;
  }

  .remove_from_cart_button {
    background-color: palevioletred;
    border: palevioletred;
    border-radius: 10px;
    box-shadow: 0 0 4px rgba(0, 0, 0, 0.25);
    padding: 10px;
    margin: 10px;
    color: #ffffff;
  }
</style>
