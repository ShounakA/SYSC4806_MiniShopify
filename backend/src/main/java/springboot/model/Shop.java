package springboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Shop {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    private String description;

    @ElementCollection
    private List<String> tags;

    @ManyToOne
    @JsonIgnoreProperties("ownedShops")
    private Owner owner;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="shop")
    @JsonIgnoreProperties("shop")
    private List<Item> products;

    public Shop() {

        tags = new ArrayList<String>();
        products = new ArrayList<Item>();
        name="";
        description="";
        owner = null;
    }

    public Shop(String name, String description, List<String> tags, Owner owner, List<Item> products) {

        this.name = name;
        this.description = description;
        this.tags = tags;
        this.owner = owner;
        this.products = products;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<Item> getProducts() {
        return products;
    }

    public void setProducts(List<Item> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", tags=" + tags +
                ", owner=" + owner.getId() +
                ", products=" + products +
                '}';
    }
}
