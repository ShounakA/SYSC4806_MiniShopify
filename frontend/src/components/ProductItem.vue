<template>
  <div class='content'>
    <div class='center'>
      <div class='flex-form'>
        <div class="item_info_container">
          <h3>Item Name:</h3>
          <h3 id='item_name'>{{name}}</h3>
        </div>
        <div class="item_info_container">
          <h3>Item Description:</h3>
          <h3 id='item_description'>{{description}}</h3>
        </div>
        <div class="item_info_container">
          <h3>Item Price:</h3>
          <h3 id='item_price'>${{stockPrice}}</h3>
        </div>
        <div class="item_info_container">
          <h3>Available Quantity:</h3>
          <h3 id='item_quantity'>{{stockQuantity}}</h3>
        </div>
      </div>
    </div>
    <div class="cart_add_options">
      <input class="form_input" type="number" min="1" v-bind:max="stockQuantity" v-model="itemQuantity"
             placeholder="Enter Quantity..."/>
      <button class='add_to_cart_button' @click='addItemToCart'>Add To Cart</button>
    </div>
  </div>
</template>

<script>
  export default {
    name: "ProductItem",
    props: {
      id: Number,
      name: String,
      description: String,
      stockPrice: Number,
      stockQuantity: Number,
    },
    data() {
      return {
        itemQuantity: 1,
      }
    },
    methods: {
      addItemToCart() {
        if (this.itemQuantity <= this.stockQuantity) {
          this.$parent.addItemToCart(this.id, this.itemQuantity);
        } else {
          console.log("Requested Item Stock Unavailable")
        }
      },
    },
  }
</script>

<style scoped>
  .content {
    flex: 1;
    margin: 10px 5%;
    background: #A3CBFF;
    box-shadow: 0 0 4px rgba(0, 0, 0, 0.25);
    border-radius: 49px;
    text-align: center;
  }

  .item_info_container {
    display: flex;
    flex-direction: row;
    justify-content: center;
  }

  .cart_add_options {
    display: flex;
    flex-direction: row;
    justify-content: center;
  }


  h3 {
    margin: 4px;
  }

  .add_to_cart_button {
    background-color: #2ecc71;
    border: #2ecc71;
    border-radius: 10px;
    box-shadow: 0 0 4px rgba(0, 0, 0, 0.25);
    padding: 10px;
    margin: 10px;
    color: #ffffff;
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
    margin: 10px;
    padding: 10px;
    border-radius: 10px;
    border: #FFFFFF;
    box-shadow: 0 0 4px rgba(0, 0, 0, 0.25);
  }
</style>
