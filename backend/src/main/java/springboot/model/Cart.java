package springboot.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Owner owner;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart")
    private List<CartItem> items;

    public Cart() {

        items = new ArrayList<CartItem>();
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

        for (CartItem i: items) {
            if (i.getId() == itemId) {
                items.remove(i);
            }
        }
    }

    public boolean containsItem (long itemId) {
        for (CartItem i: items) {
            if (i.getId() == itemId) {
                return true;
            }
        }
        return false;
    }

    public CartItem getItem (long itemId) {
        for (CartItem i: items) {
            if (i.getId() == itemId) {
                return i;
            }
        }
        return null;
    }

    public void updateItem (long itemId, int quantity) {
        for (CartItem i: items) {
            if (i.getId() == itemId) {
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
