package springboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Owner {

    @Id
    @GeneratedValue
    private long id;

    private String firstName;
    private String lastName;

    private String email;
    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    @JsonIgnoreProperties("owner")
    private List<Shop> ownedShops;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "owner")
    @JsonIgnoreProperties("owner")
    private Cart personalCart;

    public Owner() {
        //need them to not be null thanks :).
        ownedShops = new ArrayList<Shop>();
        personalCart=new Cart();
        firstName="";
        lastName="";
    }

    public Owner(String firstName, String lastName, String email, String password) {

        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Shop> getOwnedShops() {
        return ownedShops;
    }

    public void setOwnedShops(List<Shop> ownedShop) {
        this.ownedShops = ownedShop;
    }

    public void addShop(Shop shop) {

        this.ownedShops.add(shop);
    }

    public void removeShop(Shop shop) {

        this.ownedShops.remove(shop);
    }

    public Cart getPersonalCart() {
        return personalCart;
    }

    public void setPersonalCart(Cart personalCart) {
        this.personalCart = personalCart;
    }

//    public boolean isMerchant() {
//
//        return ownedShops.size() == 0 ? true: false;
//    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", ownedShop=" + ownedShops +
                ", personalCart=" + personalCart.getId() +
                '}';
    }
}
