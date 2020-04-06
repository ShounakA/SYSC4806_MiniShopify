# SYSC4806_MiniShopify

## Project Topic

Merchant can create a new shop by filling in a form containing: the name of the shop, the categories/tags relevant to the shop, and other fields that are up to you. The Merchant can upload products to populate the shop along with a description, picture, and inventory number. Customers can find a shop by looking up the name of the shop or searching by category/tag. Once they find the shop the want, they can browse through the product catalog of the shop, and can then decide to purchase one or many products by putting them in the Shopping Cart and proceeding to Checkout. The purchase itself will obviously be simulated, but purchases cannot exceed the inventory.

## Setup Instructions
Can access H2 Console at http://localhost:8080/h2-console. The database will save to the path /data/MiniShopify. It can be altered by changing the field spring.datasource.url=jdbc:h2:file:/data/MiniShopify in the application.properties files. If tables need to be precreated create a data.sql file in main/java/resources and Spring Boot will do it automatically.

## Heroku App
link: [mini-shopify-4806](https://mini-shopify-4806.herokuapp.com/)

## Weekly Scrums

Feb 18 - [#19](https://github.com/jobinmatt/SYSC4806_MiniShopify/issues/19)
Feb 26 - [#26](https://github.com/jobinmatt/SYSC4806_MiniShopify/issues/26)
Mar 12 - [#62](https://github.com/jobinmatt/SYSC4806_MiniShopify/issues/62)
Mar 24 - [#84](https://github.com/jobinmatt/SYSC4806_MiniShopify/issues/84)
## DEMO

1. From main page /#/ navigate to /signup by clicking the Sign Up button.
2. Once you are on the sign page. Enter in your information into the form. If successful, you will be directed to login page.
3. Enter in credentials, if successful the navbar changes and you have more options for more features on the top right.
4. Cart Option -> Takes you to cart where you can view items you would like to checkout.
5. All Shops -> Lets you view all shops, yours and others. Clicking on a shop card will takes you to the Shops detailed page, where you can purchase items.
6. Add Shop -> lets you add shop, with products.
7. Search -> lets you search for a shop by a tag, name, or description.



## Page Routes

### /#/
- landing page
- here you can login or signup

### /#/signup
- signup page
- sign up for an account. need one to access most features.

### /#/cart
- cart page
- see wats in you cart

### /#/login
- login page
- here is where you can get access to other features

### /#/shop?{shopId}
- single shop page detailed view
- view a single shop in a more detailed view and purchase items.

### /#/create
- create shop
- can create a new shop with new products, inventory, and tags.

### /#/edit?{shopId}
- edit a shop
- can edit a certain shop given the id, and you are the owner.

### /#/all_shops
- view multiple shops
- can view all shops or just your own shops.

### /#/search
- search for shops
- can search by name, description, or tags.

## API Endpoints
### /api/cart/add?{ownerID}  
- Add item to user cart
- POST
- consumes: "application/json"
- header: 
     authorization: Bearer {token}
- Params: 
    RequestBody:
        {
            id: "",
            shopId: ""
        }
- Response:
    HTTP Code: 200
    Body: "Item added to users cart"
- Error:
    HTTP Code: 400
    Body: Error cause

### /api/cart/remove?{ownerID} 
- Remove item to user cart 
- POST
- consumes: "application/json"
- header: 
     authorization: Bearer {token}
- Params: 
    RequestBody:
        {
            id: "",
            shopId: ""
        }
- Response:
    HTTP Code: 200
    Body: "Item removed from users cart"
- Error:
    HTTP Code: 400
    Body: Error cause

### /api/cart/edit?{ownerID}  
- Edit items in user cart
- POST
- consumes: "application/json"
- header: 
     authorization: Bearer {token}
- Params: 
    RequestBody:
        {
            id: "",
            shopId: "",
            name: "",
            desc: "",
            stock: int,
            price: int
        }
- Response:
    HTTP Code: 200
    Body: "Item updated from users cart"
- Error:
    HTTP Code: 400
    Body: Error cause

### /api/cart/total?{ownerID}  
- Get total in user's cart
- GET
- consumes: "application/json"
- header: 
     authorization: Bearer {token}
- Params: 
- Response:
    HTTP Code: 200
    Body: "Total : " + totalVal
- Error:
    HTTP Code: 400
    Body: Error cause

### /api/cart?{ownerID}  
- Get user Cart
- GET
- consumes: "application/json"
- header: 
     authorization: Bearer {token}
- Params: 
- Response:
    HTTP Code: 200
    Body: User Cart
- Error:
    HTTP Code: 400
    Body: Error cause

### /api/shop  
- Create new shop
- POST
- consumes: "application/json"
- header: 
     authorization: Bearer {token}
- Params: 
    RequestBody:
        {
            id: int, 
            name: String,
            desc: String,
            tags: [String],
            userId: int,
            products: [
                {
                    id: int,
                    shopId: int,
                    name: String,
                    desc: String,
                    stock: int,
                    price: int
                }
            ]
        }
- Response:
    HTTP Code: 200
    Body: Shop 
- Error:
    HTTP Code: 400
    Body: Error cause

### /api/public/shop?{shopId}  
- Get Shop
- GET
- consumes: "application/json"
- Params: 
- Response:
    HTTP Code: 200
    Body: Shop 
- Error:
    HTTP Code: 400
    Body: Error cause

### /api/shop/edit  
- Edit shop information; The the fields that are not null will be updated in the shop
- POST
- consumes: "application/json"
- header: 
     authorization: Bearer {token}
- Params: 
    RequestBody:
        {
            id: int, 
            name: String,
            desc: String,
            tags: [String],
            userId: int,
            products: [
                {
                    id: int,
                    shopId: int,
                    name: String,
                    desc: String,
                    stock: int,
                    price: int
                }
            ]
        }
- Response:
    HTTP Code: 200
    Body: Shop 
- Error:
    HTTP Code: 400
    Body: Error cause

### /api/shop?{shopId}{ownerId}  
- Delete shop
- POST
- consumes: "application/json"
- header: 
     authorization: Bearer {token}
- Params: 
    RequestBody:
        {
            id: int, 
            name: String,
            desc: String,
            tags: [String],
            userId: int,
            products: [
                {
                    id: int,
                    shopId: int,
                    name: String,
                    desc: String,
                    stock: int,
                    price: int
                }
            ]
        }
- Response:
    HTTP Code: 200
    Body: "Shop ID: " + shopId + " deleted" 
- Error:
    HTTP Code: 400
    Body: Error cause

### /api/owner
- Create Owner. If any of the fields expect Id is null it will error out.
- POST
- consumes: "application/json"
- Params: 
    RequestBody:
        {
            id: int,
            firstName: String,
            lastName: String,
            email: String,
            password: String
        }
- Response:
    HTTP Code: 200
    Body: "{'msg': 'User added','ownerId':"+user.getId()+"}" 
- Error:
    HTTP Code: 400
    Body: Error cause

### /api/login
- Log user in
- POST
- consumes: "application/json"
- Params: 
    RequestBody:
    {
        email: String,
        password: String
    }
- Response: 
    HTTP Code: 400
    Body: Owner
- Error:
    HTTP Code: 400
    Body: Error Cause

### /api/owner/getFromId?{ownerID}
- Get owner by id
- GET
- consumes: "application/json"
- header: 
     authorization: Bearer {token}
- Params: 
- Response:
    HTTP Code: 200
    Body: Owner
- Error:
    HTTP Code: 400
    Body: Error cause

### /api/owner/getFromEmail?{email}
- Get owner by email
- GET
- consumes: "application/json"
- header: 
     authorization: Bearer {token}
- Params: 
- Response:
    HTTP Code: 200
    Body: Owner
- Error:
    HTTP Code: 400
    Body: Error cause

### /api/owner
- Edit owner. Fields that are empty will not be altered
- PATCH
- consumes: "application/json"
- header: 
     authorization: Bearer {token}
- Params: 
    RequestBody:
        {
            id: int,
            firstName: String,
            lastName: String,
            email: String,
            password: String
        }
- Response:
    HTTP Code: 200
    Body: Owner
- Error:
    HTTP Code: 400
    Body: Error cause

### /api/owner?{ownerId}
- Delete Owner
- DELETE
- consumes: "application/json"
- header: 
     authorization: Bearer {token}
- Params: 
- Response:
    HTTP Code: 200
    Body: "Owner ID: " + ownerId  +  " with all shops deleted"
- Error:
    HTTP Code: 400
    Body: Error cause
