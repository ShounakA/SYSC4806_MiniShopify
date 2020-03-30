package springboot.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import springboot.model.Shop;


public class ItemDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("shopId")
    private Long shopId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("desc")
    private String description;

    @JsonProperty("stock")
    private int stockQuantity;

    @JsonProperty("price")
    private double price;

    public ItemDTO() {
    }

    public ItemDTO(Long id, String name, String description, int stockQuantity, double price, Long shopId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.shopId = shopId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
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

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
