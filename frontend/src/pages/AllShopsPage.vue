<template>
  <div class="content">
    <div>
      <h2><b>SHOPS</b></h2>
      <div class="toggle_container">
        <h3 class="toggle_label">All Shops</h3>
        <label class="toggle">
          <input type="checkbox" v-on:click="toggleShops">
          <span class="slider round"></span>
        </label>
        <h3 class="toggle_label">Your Shops</h3>
      </div>
      <div class="flex-form">
        <ShopItem v-for="shop in shops" :key="shop.id" v-bind:name="shop.name" v-bind:id="shop.id"
                  v-bind:description="shop.description"
                  v-bind:owner-name="shop.owner.firstName +' '+ shop.owner.lastName" v-bind:shopOwnerId="shop.owner.id"
                  v-bind:currentUserId="userId"/>
      </div>
    </div>
  </div>
</template>

<script>
  import ShopItem from "../components/ShopItem";
  import {getCookie, OWNER_ID_HEADER_STRING, STATUS_OK_CODE, TOKEN_COOKIE_HEADER,} from "../constants/constants";
  import axios from "axios";

  export default {
    name: "AllShopsPage",
    components: {
      ShopItem
    },
    mounted() {
      if (getCookie(TOKEN_COOKIE_HEADER) !== '') {
        let token = JSON.parse(getCookie(TOKEN_COOKIE_HEADER));
        this.userId = token[OWNER_ID_HEADER_STRING];
      }
      this.loadShops();
    },
    data() {
      return {
        userId: null,
        displayAll: true,
        allShops: [],
        userShops: [],
        shops: [],
      }
    },
    methods: {
      loadShops() {
        let currentUserId = this.userId;
        let userShops = [];
        axios.get('/api/public/allShops')
          .then((response) => {
            console.log(response)
            this.shops = response.data;
            if (response.status === STATUS_OK_CODE) {
              if (currentUserId !== null) {
                response.data.forEach(function (shop) {
                  if (currentUserId == shop.owner.id) {
                    userShops.push(shop);
                  }
                });
                this.userShops = userShops;
              }
              this.shops = response.data;
              this.allShops = response.data;
            }
          })
          .catch((error) => {
            console.log(error);
          });
      },
      toggleShops() {
        if (this.displayAll) {
          this.shops = this.userShops;
          this.displayAll = false;
        } else {
          this.shops = this.allShops;
          this.displayAll = true;
        }
      },
    },
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

  .toggle_container {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;
  }

  .toggle_label {
    margin: 0 4px;
  }

  .toggle {
    position: relative;
    display: inline-block;
    width: 38px;
    height: 20px;
  }

  .toggle input {
    opacity: 0;
    width: 0;
    height: 0;
  }

  .slider {
    position: absolute;
    cursor: pointer;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: #ccc;
    -webkit-transition: .4s;
    transition: .4s;
  }

  .slider:before {
    position: absolute;
    content: "";
    height: 18px;
    width: 18px;
    left: 1px;
    bottom: 1px;
    background-color: white;
    -webkit-transition: .4s;
    transition: .4s;
  }

  input:checked + .slider {
    background-color: #a3cbff;
  }

  input:focus + .slider {
    box-shadow: 0 0 1px #a3cbff;
  }

  input:checked + .slider:before {
    -webkit-transform: translateX(18px);
    -ms-transform: translateX(18px);
    transform: translateX(18px);
  }

  .slider.round {
    border-radius: 34px;
  }

  .slider.round:before {
    border-radius: 50%;
  }

  .flex-form {
    display: flex;
    flex-direction: column;
    justify-content: center;
  }
</style>
