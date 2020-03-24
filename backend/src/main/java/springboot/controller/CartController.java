package springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springboot.DTO.ItemDTO;
import springboot.Repository.ItemRepository;
import springboot.Repository.OwnerRepository;
import springboot.Repository.ShopRepository;
import springboot.model.CartItem;
import springboot.model.Item;
import springboot.model.Owner;
import springboot.model.Shop;

import java.util.Optional;

@RestController
public class CartController {

    private OwnerRepository ownerRepo;
    private ShopRepository shopRepo;
    private ItemRepository itemRepo;

    @Autowired
    public CartController(OwnerRepository userRepo, ShopRepository shopRepo, ItemRepository itemRepo ){
        this.ownerRepo = userRepo;
        this.shopRepo = shopRepo;
        this.itemRepo = itemRepo;
    }

    @RequestMapping(value = "/api/cart/add",method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity addItem(@RequestParam(value = "ownerID") Long ownerID, @RequestBody ItemDTO itemDto){
        if(itemDto != null && itemDto.getId() != null && !isEmptyNull(itemDto.getName()) && itemDto.getShopId() != null) {

            Optional<Owner> user = ownerRepo.findById(ownerID);
            if (!user.isPresent()) {
                return ResponseEntity.badRequest().body("Owner does not exists");
            }

            Optional<Shop> shop = shopRepo.findById(itemDto.getShopId());
            if (!shop.isPresent()) {
                return ResponseEntity.badRequest().body("Shop does not exist from which you want to add item from");
            }

            Shop sellerShop = shop.get();

            Optional<Item> item = itemRepo.findById(itemDto.getId());
            if (item.isPresent()) {
                Item foundItem = item.get();
                if (foundItem.getStockQuantity() >= itemDto.getStockQuantity()) {
                    foundItem.setStockQuantity(foundItem.getStockQuantity() - itemDto.getStockQuantity());
                    itemRepo.save(foundItem);
                    //update repo
                    CartItem itemToAdd = new CartItem(foundItem.getId(), itemDto.getStockQuantity());
                    user.get().getPersonalCart().addItem(itemToAdd);
                    ownerRepo.save(user.get());
                    return ResponseEntity.ok().body("Item added to users cart");
                } else {
                    return ResponseEntity.badRequest().body("Not enough stock");
                }
            } else {
                return ResponseEntity.badRequest().body("Item not found");
            }
        } else {
            return ResponseEntity.badRequest().body("Not enough information provided");
        }
    }

    @RequestMapping(value = "/api/cart/remove",method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity removeItem(@RequestParam(value = "ownerID") Long ownerID, @RequestBody ItemDTO itemDto){
        if(itemDto != null && itemDto.getId() != null && !isEmptyNull(itemDto.getName()) && itemDto.getShopId() != null) {

            Optional<Owner> user = ownerRepo.findById(ownerID);
            if (!user.isPresent()) {
                return ResponseEntity.badRequest().body("Owner does not exists");
            }

            if (!user.get().getPersonalCart().containsItem(itemDto.getId())){
                return ResponseEntity.badRequest().body("Item does not exist is users cart");
            }

            Optional<Shop> shop = shopRepo.findById(itemDto.getShopId());
            if (!shop.isPresent()) {
                user.get().getPersonalCart().removeItem(itemDto.getId());
                ownerRepo.save(user.get());
                return ResponseEntity.badRequest().body("Shop does not exist from which you want to remove item from");
            }

            Shop sellerShop = shop.get();

            Optional<Item> item = itemRepo.findById(itemDto.getId());
            if (item.isPresent()) {
                Item foundItem = item.get();
                foundItem.setStockQuantity(foundItem.getStockQuantity() + itemDto.getStockQuantity());
                itemRepo.save(foundItem);
                //update repo
                user.get().getPersonalCart().removeItem(itemDto.getId());
                ownerRepo.save(user.get());
                return ResponseEntity.ok().body("Item removed from users cart");

            } else {
                return ResponseEntity.badRequest().body("Item not found");
            }
        } else {
            return ResponseEntity.badRequest().body("Not enough information provided");
        }
    }

    @RequestMapping(value = "/api/cart/edit",method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity editItem(@RequestParam(value = "ownerID") Long ownerID, @RequestBody ItemDTO itemDto){

        if(itemDto != null && itemDto.getId() != null && !isEmptyNull(itemDto.getName()) && itemDto.getShopId() != null) {

            Optional<Owner> user = ownerRepo.findById(ownerID);
            if (!user.isPresent()) {
                return ResponseEntity.badRequest().body("Owner does not exists");
            }

            if (!user.get().getPersonalCart().containsItem(itemDto.getId())){
                return ResponseEntity.badRequest().body("Item does not exist is users cart");
            }

            Optional<Shop> shop = shopRepo.findById(itemDto.getShopId());
            if (!shop.isPresent()) {
                user.get().getPersonalCart().removeItem(itemDto.getId());
                ownerRepo.save(user.get());
                return ResponseEntity.badRequest().body("Shop does not exist from which you want to edit item from");
            }

            Shop sellerShop = shop.get();

            Optional<Item> item = itemRepo.findById(itemDto.getId());
            if (item.isPresent()) {
                Item foundItem = item.get();
                int currentAmount = user.get().getPersonalCart().getItem(itemDto.getId()).getQuantity();
                if (itemDto.getStockQuantity() - currentAmount > 0) {
                    if (foundItem.getStockQuantity() >= (itemDto.getStockQuantity() - currentAmount)) {
                        foundItem.setStockQuantity(foundItem.getStockQuantity() - (itemDto.getStockQuantity() - currentAmount));
                    }
                } else {
                    foundItem.setStockQuantity(foundItem.getStockQuantity() + (currentAmount - itemDto.getStockQuantity()));
                }
                itemRepo.save(foundItem);
                //update repo
                user.get().getPersonalCart().updateItem(itemDto.getId(), itemDto.getStockQuantity());
                ownerRepo.save(user.get());
                return ResponseEntity.ok().body("Item updated from users cart");

            } else {
                return ResponseEntity.badRequest().body("Item not found");
            }
        } else {
            return ResponseEntity.badRequest().body("Not enough information provided");
        }
    }

    @RequestMapping(value = "/api/cart/total",method = RequestMethod.GET, consumes = "application/json")
    public ResponseEntity total(@RequestParam(value = "ownerID") Long ownerID){

        double total = 0;
        Optional<Owner> user = ownerRepo.findById(ownerID);
        if (!user.isPresent()) {
            return ResponseEntity.badRequest().body("Owner does not exists");
        }

        if (user.get().getPersonalCart().getItems().isEmpty()){
            return ResponseEntity.ok().body("Total: " + total);
        }

        for (CartItem item: user.get().getPersonalCart().getItems()) {
            Optional<Item> found = itemRepo.findById(item.getId());
            if (found.isPresent()) {
                total += found.get().getPrice() * item.getQuantity();
            }
        }
        return ResponseEntity.ok().body("Total: " + total);
    }

    @RequestMapping(value = "/api/cart",method = RequestMethod.GET, consumes = "application/json")
    public ResponseEntity getCart(@RequestParam(value = "ownerID") Long ownerID){

        Optional<Owner> user = ownerRepo.findById(ownerID);
        if (!user.isPresent()) {
            return ResponseEntity.badRequest().body("Owner does not exists");
        }

        return ResponseEntity.ok(user.get().getPersonalCart());
    }

    //True if null or empty
    public boolean isEmptyNull( String s) {
        return s == null || s.trim().isEmpty();
    }
}
