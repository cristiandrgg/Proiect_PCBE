package models;

import java.util.ArrayList;
import java.util.List;

public class Buyer {

    private Integer id;
    private String name;
    private List<Bid> bidList;
    private List<Stock> stockList;

    public Buyer(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.bidList = new ArrayList<>();
        this.stockList = new ArrayList<>();
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

    public List<Bid> getBidList() {
        return bidList;
    }

    public void setBidList(List<Bid> bidList) {
        this.bidList = bidList;
    }

    public List<Stock> getStockList() {
        return stockList;
    }

    public void setStockList(List<Stock> stockList) {
        this.stockList = stockList;
    }
}