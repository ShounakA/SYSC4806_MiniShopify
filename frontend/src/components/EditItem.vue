<template>
  <div class='content'>
    <div class='center'>
      <h2><b>CREATE AN ITEM</b></h2>
      <div class='flex-form'>
        <h2>ITEM NAME:</h2>
        <input class='form_input' type='text' v-model='name' placeholder='Enter item name...' id='item_name'><br>
        <h2>ITEM DESCRIPTION:</h2>
        <input class='form_input' type='text' v-model='description' placeholder='Enter item description...'
               id='item_description'><br>
        <h2>QUANTITY:</h2>
        <input class='form_input' type='number' min='0' v-model='stockQuantity' placeholder='Enter item quantity...'
               id='item_quantity'><br>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      name: '',
      description: '',
      stockQuantity: -1
    }
  },
  name: 'EditItem',
  methods: {}
}
</script>

<style scoped>
  .content {
    margin: 2% 15% 0% 15%;
    background: #A3CBFF;
    box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
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
    margin: 10px;
    padding: 10px;
    border-radius: 10px;
  }
</style>
