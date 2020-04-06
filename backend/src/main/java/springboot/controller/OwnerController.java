package springboot.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springboot.DTO.OwnerDTO;
import springboot.Repository.OwnerRepository;
import springboot.Repository.ShopRepository;
import springboot.model.Owner;
import springboot.model.Shop;

import java.util.Optional;

@RestController
public class OwnerController {

    private OwnerRepository ownerRepo;
    private ShopRepository shopRepo;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public OwnerController(OwnerRepository userRepo, ShopRepository shopRepo, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.ownerRepo = userRepo;
        this.shopRepo = shopRepo;
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
    }

    @RequestMapping(value = "/api/owner",method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity createOwner(@RequestBody OwnerDTO newOwner){
        if(newOwner != null && !isEmptyNull(newOwner.getFirstName()) && !isEmptyNull(newOwner.getLastName()) && !isEmptyNull(newOwner.getEmail())
                && !isEmptyNull(newOwner.getPassword())){

            if (newOwner.getId() != null) {
                Optional<Owner> userId = ownerRepo.findById(newOwner.getId());
                if (userId.isPresent()) {
                    return ResponseEntity.badRequest().body("User already exists");
                }
            }

            Owner user = ownerRepo.findByEmail(newOwner.getEmail());
            if(user != null){
                return ResponseEntity.badRequest().body("Email already exists");
            }

            user = new Owner();
            user.setFirstName(newOwner.getFirstName());
            user.setLastName(newOwner.getLastName());
            user.setEmail(newOwner.getEmail());
            user.setPassword(bCryptPasswordEncoder.encode(newOwner.getPassword()));
            user.getPersonalCart().setOwner(user);
            ownerRepo.save(user);
            return ResponseEntity.ok().body("{'msg': 'User added','ownerId': "+ user.getId()+"}"); //Everything is OK so we send the okay response with the new shop in the body
        } else {
            return ResponseEntity.badRequest().body("Owner object was null");
        }
    }

    @RequestMapping(value = "/api/login", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity login (@RequestBody OwnerDTO owner) {
        if (owner != null && !isEmptyNull(owner.getEmail()) && !isEmptyNull(owner.getPassword())) {

            Owner user = ownerRepo.findByEmail(owner.getEmail());

            if (user == null) {
                return ResponseEntity.badRequest().body("Email not found");
            }
            if (bCryptPasswordEncoder.encode(owner.getPassword()).equals(user.getPassword())) {
                return ResponseEntity.ok().body("{'msg': 'Login successful','ownerId':"+user.getId()+"}");
            } else {
                return ResponseEntity.badRequest().body("Wrong password");
            }
        } else {
            return ResponseEntity.badRequest().body("Owner email and password is missing");
        }
    }

    @GetMapping(value = "/api/owner/getFromId")
    public ResponseEntity getOwnerFromID(@RequestParam(value = "ownerID") Long ownerID){
        if(ownerID != null){
            Optional<Owner> user = ownerRepo.findById(ownerID);
            if(user.isPresent()){
                Owner returnUser = user.get();
                returnUser.setPassword("");
                return ResponseEntity.ok(returnUser);
            } else {
                return ResponseEntity.badRequest().body("Owner ID: " + ownerID + " not found");
            }
        } else {
            return ResponseEntity.badRequest().body("Owner ID is empty");
        }
    }

    @GetMapping(value = "/api/owner/getFromEmail")
    public ResponseEntity getOwnerFromEmail(@RequestParam(value = "email") String email){
        if(email != null){
            Owner user = ownerRepo.findByEmail(email);
            if(user != null){
                Owner returnUser = user;
                returnUser.setPassword("");
                return ResponseEntity.ok(returnUser);
            } else {
                return ResponseEntity.badRequest().body("Owner email: " + email + " not found");
            }
        } else {
            return ResponseEntity.badRequest().body("Owner email is empty");
        }
    }

    @RequestMapping(value = "/api/owner/edit", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity editOwner(@RequestBody OwnerDTO editOwner){
        if(editOwner != null && editOwner.getId() != null){
            Optional<Owner> user = ownerRepo.findById(editOwner.getId());
            if(user.isPresent()){
                Owner newUser = user.get();
                if(!isEmptyNull(editOwner.getFirstName()) && !newUser.getFirstName().equals(editOwner.getFirstName())){
                    newUser.setFirstName(editOwner.getFirstName());
                }

                if(!isEmptyNull(editOwner.getLastName()) && !newUser.getLastName().equals(editOwner.getLastName())){
                    newUser.setLastName(editOwner.getLastName());
                }

                if(!isEmptyNull(editOwner.getEmail()) && !newUser.getEmail().equals(editOwner.getEmail())){
                    newUser.setEmail(editOwner.getEmail());
                }

                if(!isEmptyNull(editOwner.getPassword()) && !newUser.getPassword().equals(editOwner.getPassword())){
                    newUser.setPassword(editOwner.getPassword());
                }

                ownerRepo.save(newUser);

                return ResponseEntity.ok(newUser);

            } else {
                return ResponseEntity.badRequest().body("Owner ID: " + editOwner.getId() + " not found");
            }
        } else {
            return ResponseEntity.badRequest().body("Owner null or no id found");
        }
    }

    @DeleteMapping(value = "/api/owner")
    public ResponseEntity deleteOwner(@RequestParam(value = "ownerId") Long ownerId){
        if(ownerId != null){
            Optional<Owner> owner = ownerRepo.findById(ownerId);
            if(owner.isPresent()){
                for(Shop s : owner.get().getOwnedShops()){
                    shopRepo.delete(s);
                    }
            }
        } else {
            return ResponseEntity.badRequest().body("Owner ID is null");
        }
        return ResponseEntity.ok("Owner ID: " + ownerId  +  " with all shops deleted");
    }

    //True if null or empty
    public boolean isEmptyNull( String s ) {
        return s == null || s.trim().isEmpty();
    }
}
