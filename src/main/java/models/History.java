package models;

public class History {

    private Integer tranzId;
    private Integer stockSellId;
    private Integer bidId;
    private Integer numberOfActions;
    private float price;

    public History(Integer tranzId, Integer stockSellId, Integer bidId, Integer numberOfActions, float price) {
        this.tranzId = tranzId;
        this.stockSellId = stockSellId;
        this.bidId = bidId;
        this.numberOfActions = numberOfActions;
        this.price = price;
    }

    public Integer getTranzId() {
        return tranzId;
    }

    public void setTranzId(Integer tranzId) {
        this.tranzId = tranzId;
    }

    public Integer getStockSellId() {
        return stockSellId;
    }

    public void setStockSellId(Integer stockSellId) {
        this.stockSellId = stockSellId;
    }

    public Integer getBidId() {
        return bidId;
    }

    public void setBidId(Integer bidId) {
        this.bidId = bidId;
    }

    public Integer getNumberOfActions() {
        return numberOfActions;
    }

    public void setNumberOfActions(Integer numberOfActions) {
        this.numberOfActions = numberOfActions;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
