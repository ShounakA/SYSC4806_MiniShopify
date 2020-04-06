package springboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties("owner")
    private Owner owner;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart")
    @JsonIgnoreProperties("cart")
    private List<CartItem> items;

    public Cart() {

        items = new ArrayList<CartItem>();
        owner = null;
    }

    public Cart(Owner owner) {

        this.owner = owner;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public void addItem(CartItem item) {

        this.items.add(item);
    }

    public void removeItem(long itemId) {
        CartItem toRemove = null;
        for (CartItem i: items) {
            if (i.getItemId() == itemId) {
//                items.remove(i);
                toRemove = i;
            }
        }
        if(toRemove != null){
            toRemove.setCart(null);
            items.remove(toRemove);
        }
    }

    public boolean containsItem (long itemId) {
        for (CartItem i: items) {
            if (i.getItemId() == itemId) {
                return true;
            }
        }
        return false;
    }

    public CartItem getItem (long itemId) {
        for (CartItem i: items) {
            if (i.getItemId() == itemId) {
                return i;
            }
        }
        return null;
    }

    public void updateItem (long itemId, int quantity) {
        for (CartItem i: items) {
            if (i.getItemId() == itemId) {
                i.setQuantity(quantity);
            }
        }
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", owner=" + owner.getId() +
                ", items=" + items +
                '}';
    }
}
