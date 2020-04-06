<template>
  <div class="content" v-on:click="selectShop">
    <div class="center">
      <h3><b>Name: {{name}}</b></h3>
      <div class="flex-form">
        <h4>Description: {{description}}</h4>
        <h4>Owner: {{ownerName}}</h4>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "ShopItem",
    props: {
      name: String,
      id: Number,
      description: String,
      ownerName: String,
      shopOwnerId: Number,
      currentUserId: Number
    },
    methods: {
      selectShop() {
        if (this.shopOwnerId == this.currentUserId) {
          this.$router.push({name: 'edit', query: {shopId: this.id.toString()}});
        } else {
          this.$router.push({name: 'shop', query: {shopId: this.id.toString()}});
        }
      },
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
</style>
