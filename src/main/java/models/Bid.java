package models;

public class Bid {

    private Integer id;
    private Integer buyerId;
    private Integer stockId;
    private float price;
    private Integer numberOfStocks;

    public Bid(Integer id, Integer buyerId, Integer stockId, float price, Integer numberOfStocks) {
        this.id = id;
        this.buyerId = buyerId;
        this.stockId = stockId;
        this.price = price;
        this.numberOfStocks = numberOfStocks;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Integer getNumberOfStocks() {
        return numberOfStocks;
    }

    public void setNumberOfStocks(Integer numberOfStocks) {
        this.numberOfStocks = numberOfStocks;
    }
}