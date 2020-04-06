<template>
  <div class='content'>
    <div class='center'>
      <h2><b>EDIT YOUR ITEM</b></h2>
      <div class='flex-form'>
        <label for='item_name'>Item Name:</label><input class='form_input' type='text' v-model='name'
                                                        placeholder='Enter item name...' id='item_name'/>
        <label for='item_description'>Item Description:</label><input class='form_input' type='text'
                                                                      v-model='description'
                                                                      placeholder='Enter item description...'
                                                                      id='item_description'/>
        <label for='item_price'>Item Price:</label><input class='form_input' type='number' min='0.00' step="1.00"
                                                          v-model='stockPrice'
                                                          placeholder='Enter item price...'
                                                          id='item_price'/>
        <label for='item_quantity'>Item Quantity:</label><input class='form_input' type='number' min='0'
                                                                v-model='stockQuantity'
                                                                placeholder='Enter item quantity...'
                                                                id='item_quantity'/>
      </div>
    </div>
    <button id='remove_button' @click='removeItem'>Remove Item</button>
  </div>
</template>

<script>
  export default {
    props: {index: Number},
    mounted() {
      this.$parent.addItem();
    },
    data() {
      return {
        name: '',
        description: '',
        stockPrice: '',
        stockQuantity: ''
      }
    },
    watch: {
      name: function () {
        this.updateItem()
      },
      description: function () {
        this.updateItem()
      },
      stockPrice: function () {
        this.updateItem()
      },
      stockQuantity: function () {
        this.updateItem()
      },
    },
    name: 'EditItem',
    methods: {
      updateItem() {
        const item = {
          name: this.name,
          desc: this.description,
          price: this.stockPrice,
          stock: this.stockQuantity
        }
        this.$parent.updateItem(this.$props.index, item);
      },
      removeItem() {
        this.$parent.removeItemComponent(this.$props.index);
      }
    }
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

  #remove_button {
    background-color: palevioletred;
    border: palevioletred;
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
