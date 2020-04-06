<template>
  <div class="content">
    <div>
      <h1><b>SEARCH SHOPS</b></h1>
      <div>
        <input v-model='queryName' type="text" placeholder="Search by name of shop"/>
        <input v-model='queryDesc' type="text" placeholder="Search by description of shop"/>
        <input-tag class='tag' v-model="queryTags" placeholder="Search by tags"></input-tag>
        <button class='search' type="button" @click="loadShops">Search</button>
      </div>
      <ShopItem v-for="shop in shops" :key="shop.id" v-bind:name="shop.name" v-bind:description="shop.description"
                  v-bind:owner-name="shop.owner.firstName +' '+ shop.owner.lastName"/>
      <h2 v-if='!found'><b>{{ message }}</b></h2>
    </div>
  </div>
</template>
<script>
  import ShopItem from "../components/ShopItem"
  import InputTag from 'vue-input-tag'
  import {
    getCookie,
    OWNER_ID_HEADER_STRING,
    STATUS_OK_CODE,
    TOKEN_COOKIE_HEADER,
  } from "../constants/constants"
  import axios from "axios"

  export default {
    name: "MerchantShops",
    components: {
      ShopItem,
      InputTag
    },
    mounted() {
      if (getCookie(TOKEN_COOKIE_HEADER) !== '') {
        var token = JSON.parse(getCookie(TOKEN_COOKIE_HEADER))
        this.userId = token[OWNER_ID_HEADER_STRING]
      }
      console.log(this.queryDesc)
      console.log(this.queryName)
      console.log(this.queryTags)
      // this.loadShops()
    },
    data() {
      return {
        userId: null,
        shops: [],
        queryName:'',
        queryDesc:'',
        queryTags:[],
        found:false,
        message:'Enter some search criteria above.'
      }
    },
    methods: {
      loadShops() {
        var currentUserId = this.userId;
        this.shops=[]
        var userShops = [];
        if ((this.queryTags === null || this.queryTags.length===0) && (this.queryName===null || this.queryName==='') && (this.queryDesc===null || this.queryDesc==='')){
          this.message = 'Enter in at least one field.'
        }else{
          axios.post('/api/public/search',
            {
              name:this.queryName,
              desc:this.queryDesc,
              tags:this.queryTags
            },
            { headers:{ 'Content-Type': 'application/json'}},
          )
            .then((response) => {
              if (response.status === STATUS_OK_CODE) {
                this.shops = response.data;
                this.found = true;
              }else{
                this.found = false;
                this.message='No shops with provided criteria found.'
              }
            })
            .catch((error) => {
              console.log(error);
              this.message = 'Error occurred. Please try again.'
            });
        }

      },
    }
  }
</script>
<style scoped>
  .content {
    margin: 16px 15% 0 15%;
    background: #DDECFF;
    box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
    border-radius: 49px;
    padding-top: 5%;
    padding-bottom: 5%;
    text-align: center;
  }

  .flex-form {
    display: flex;
    /*flex-direction: column;*/
    flex-wrap: wrap;
    justify-content: center;
  }
  .search{
    /*padding: 5% 10% 5% 10%;*/
    margin: auto;
    background: #007DC4;
    border-radius: 8px;
    box-shadow: 2px 4px 4px rgba(0, 0, 0, 0.1);
    color: white;
  }
  .tag{
    padding: 12px 20px;
    margin: 8px 4px;
    box-sizing: border-box;
    border-radius: 10px;
  }
</style>
