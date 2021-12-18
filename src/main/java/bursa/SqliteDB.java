package bursa;

import models.Bid;
import models.Buyer;
import models.Seller;
import models.Stock;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteDB {

    Connection connection = null;
    Statement statement = null;

    public SqliteDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:bursa.db");
            System.out.println("Connected to the db");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public List<Buyer> getBuyers() {
        List<Buyer> list = new ArrayList<>();

        try {
            this.statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM BUYERS");

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("nume");
                Buyer buyer = new Buyer(id, name);

                // bids
                List<Bid> bidList = getBids();
                List<Bid> buyerBidsList = new ArrayList<>();
                for (Bid bid : bidList) {
                    if (bid.getBuyerId().equals(buyer.getId())) {
                        buyerBidsList.add(bid);
                    }
                }
                buyer.setBidList(buyerBidsList);
                list.add(buyer);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    public List<Seller> getSellers() {
        List<Seller> list = new ArrayList<>();

        try {
            this.statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM SELLERS");

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("nume");
                Seller seller = new Seller(id, name);

                // stocks
                List<Stock> stockList = getStocks();
                List<Stock> sellerStocksList = new ArrayList<>();
                for (Stock stock : stockList) {
                    if (stock.getSellerId().equals(seller.getId())) {
                        sellerStocksList.add(stock);
                    }
                }
                seller.setStockList(sellerStocksList);
                list.add(seller);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    public List<Bid> getBids() throws SQLException {
        Statement bidStatement = connection.createStatement();
        ResultSet bidResultSet = bidStatement.executeQuery("SELECT * FROM BIDS");

        List<Bid> bidList = new ArrayList<>();

        while (bidResultSet.next()) {
            Integer bidId = bidResultSet.getInt("id");
            Integer buyerId = bidResultSet.getInt("id_buyer");
            Integer stockId = bidResultSet.getInt("id_actiune");
            float price = bidResultSet.getFloat("pret");
            Integer stockNumber = bidResultSet.getInt("nr_actiuni");

            Bid bid = new Bid(bidId, buyerId, stockId, price, stockNumber);
            bidList.add(bid);
        }

        return bidList;
    }

    public List<Stock> getStocks() throws SQLException {
        // stocks
        Statement stockStatement = connection.createStatement();
        ResultSet stockResultSet = stockStatement.executeQuery("SELECT STOCKS.id, STOCKS.nume, SELLS.nr_actiuni, SELLS.pret, SELLS.id_seller FROM STOCKS, SELLS WHERE STOCKS.id = SELLS.id;");

        List<Stock> stockList = new ArrayList<>();

        while (stockResultSet.next()) {
            Integer stockId = stockResultSet.getInt("id");
            String stockName = stockResultSet.getString("nume");
            Integer stockNumber = stockResultSet.getInt("nr_actiuni");
            float price = stockResultSet.getFloat("pret");
            Integer sellerId = stockResultSet.getInt("id_seller");

            Stock stock = new Stock(stockId, stockName, stockNumber, price, sellerId);
            stockList.add(stock);
        }
        return stockList;
    }

    public void updateBid(Bid bid, boolean modify) throws SQLException {
        int random = (int) (Math.random() * 4);
        float changedPrice;
        if (!modify) {
            changedPrice = bid.getPrice() - random;
        } else {
            changedPrice = bid.getPrice() + random;
        }

        Statement stockStatement = connection.createStatement();
        int rowsCount = stockStatement.executeUpdate(String.format("UPDATE BIDS SET pret=%s WHERE id=%s", changedPrice, bid.getId()));

        if (rowsCount == 1) {
            System.out.printf("Bid price has changed to %s", changedPrice);
        } else {
            System.out.println("Bid price hasn't changed");
        }
    }

    public void updateStock(Stock stock, boolean modify) throws SQLException {
        int random = (int) (Math.random() * 4);
        float changedPrice;
        if (!modify) {
            changedPrice = stock.getPrice() + random;
        } else {
            changedPrice = stock.getPrice() - random;
        }


        Statement stockStatement = connection.createStatement();
        int rowsCount = stockStatement.executeUpdate(String.format("UPDATE SELLS SET pret=%s WHERE id_stock=%s", changedPrice, stock.getId()));

        if (rowsCount == 1) {
            System.out.printf("Stock price has changed to %s", changedPrice);
        } else {
            System.out.println("Stock price hasn't changed");
        }
    }
}
