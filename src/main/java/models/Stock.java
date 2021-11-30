package models;

public class Stock {

    private Integer id;
    private String name;
    private Integer numberOfStocks;
    private float price;
    private Integer sellerId;

    public Stock(Integer id, String name, Integer numberOfStocks, float price, Integer sellerId) {
        this.id = id;
        this.name = name;
        this.numberOfStocks = numberOfStocks;
        this.price = price;
        this.sellerId = sellerId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumberOfStocks() {
        return numberOfStocks;
    }

    public void setNumberOfStocks(Integer numberOfStocks) {
        this.numberOfStocks = numberOfStocks;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }
}