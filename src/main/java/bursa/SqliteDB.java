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

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
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
                Statement bidStatement = connection.createStatement();
                ResultSet bidResultSet = bidStatement.executeQuery(String.format("SELECT * FROM BIDS WHERE id_buyer=%s", id));

                List<Bid> bidList = new ArrayList<>();

                while (bidResultSet.next()) {
                    Integer bidId = bidResultSet.getInt("id");
                    Integer stockId = bidResultSet.getInt("id_actiune");
                    float price = bidResultSet.getFloat("pret");
                    Integer stockNumber = bidResultSet.getInt("nr_actiuni");

                    Bid bid = new Bid(bidId, id, stockId, price, stockNumber);
                    bidList.add(bid);
                }
                buyer.setBidList(bidList);

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
                List<Stock> stockList = new ArrayList<>();

                // stocks
                Statement stockStatement = connection.createStatement();
                ResultSet stockResultSet = stockStatement.executeQuery(String.format("SELECT * FROM STOCKS WHERE id_seller=%s", id));

                while (stockResultSet.next()) {
                    Integer stockId = stockResultSet.getInt("id");
                    String stockName = stockResultSet.getString("nume");
                    Integer stockNumber = stockResultSet.getInt("nr_actiuni");
                    float price = stockResultSet.getFloat("pret");

                    Stock stock = new Stock(stockId, stockName, stockNumber, price, id);
                    stockList.add(stock);
                }
                seller.setStockList(stockList);
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
        ResultSet stockResultSet = stockStatement.executeQuery("SELECT * FROM STOCKS");

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
}
