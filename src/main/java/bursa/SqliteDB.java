package bursa;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import models.Bid;
import models.Buyer;
import models.Seller;
import models.Stock;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeoutException;

public class SqliteDB {

    java.sql.Connection connection = null;
    private static int count = 0;

    public SqliteDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = (java.sql.Connection) DriverManager.getConnection("jdbc:sqlite:bursa.db");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public List<Buyer> getBuyers() {


        List<Buyer> list = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
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
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    public List<Seller> getSellers() {

        List<Seller> list = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
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
            statement.close();
            resultSet.close();
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
        bidResultSet.close();
        bidStatement.close();
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
        stockResultSet.close();
        stockStatement.close();
        return stockList;
    }

    public void updateBid(Bid bid) throws SQLException {

        int random = getRandomInt(1, 2);
        float changedPrice = bid.getPrice() + random;
        Statement bidStatement = connection.createStatement();
        int rowsCount = bidStatement.executeUpdate(String.format("UPDATE BIDS SET pret=%s WHERE id=%s", changedPrice, bid.getId()));
        bid.setPrice(changedPrice);
        if (rowsCount == 1) {
            System.out.printf("Bid %d price has changed to %s\n", bid.getId(), changedPrice);
        } else {
            System.out.println("Bid price hasn't changed");
        }
        bidStatement.close();
    }

    public void updateStock(Stock stock) throws SQLException {

        int random = getRandomInt(1, 3);
        float changedPrice = stock.getPrice() - random;
        Statement stockStatement = connection.createStatement();
        int rowsCount = stockStatement.executeUpdate(String.format("UPDATE SELLS SET pret=%s WHERE id_stock=%s", changedPrice, stock.getId()));
        stock.setPrice(changedPrice);
        if (rowsCount == 1) {
            System.out.printf("Stock %d price has changed to %s\n", stock.getId(), changedPrice);
        } else {
            System.out.println("Stock price hasn't changed");
        }
        stockStatement.close();
    }

    private int getRandomInt(int min, int max) {
        min = (int) Math.ceil(min);
        max = (int) Math.floor(max);
        return (int) Math.floor(Math.random() * (max - min) + min);
    }

    public void exchange(Stock stock, Bid bid) throws SQLException, InterruptedException {
        Semaphore mutex = new Semaphore(1);
        try {
            mutex.acquire();
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:bursa.db");

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            Statement counterStatement = connection.createStatement();
            ResultSet resultSet = counterStatement.executeQuery("SELECT MAX(id_tranz) FROM HISTORY");

            Integer numberOfStocks = null;
            Integer counter = null;

            while (resultSet.next()) {
                counter = resultSet.getInt("MAX(id_tranz)");
            }

            if (counter == null) {
                counter = 1;
            } else {
                counter++;
            }

            if (stock.getPrice() != bid.getPrice()) {
                bid.setPrice(stock.getPrice());
            }

            if (bid.getNumberOfStocks() > stock.getNumberOfStocks()) {
                numberOfStocks = stock.getNumberOfStocks();
            } else {
                numberOfStocks = stock.getNumberOfStocks() - bid.getNumberOfStocks();
            }

            Statement statement = connection.createStatement();
            Integer bidId = bid.getId();
            Integer stockId = stock.getId();

            int rowsCountInsert = statement.executeUpdate(String.format("INSERT INTO HISTORY VALUES (%s, %s, %s)", counter, stockId, bidId));

            String message = null;

            if (rowsCountInsert == 1) {
                message = String.format("Transaction with id %s between seller with id %s and buyer %s was completed", counter, bid.getBuyerId(), stock.getSellerId());
            } else {
                message = "History failed";
            }

            sendHistoryNotification(message);

            int rowsCountUpdate = statement.executeUpdate(String.format("UPDATE SELLS SET nr_actiuni=%s WHERE id_stock=%s", numberOfStocks, stock.getId()));
            if (rowsCountUpdate == 1) {
                message = String.format("Stock offer with id %s was modified", stock.getId());
            } else {
                message = "Update failed";
            }

            sendHistoryNotification(message);

            int rowsCountDeleteBid = statement.executeUpdate(String.format("DELETE FROM BIDS WHERE id_actiune=%s", bid.getStockId()));

            if (rowsCountDeleteBid == 1) {
                message = String.format("Bid with id %s was completed", bid.getId());
            } else {
                message = "Bid deletion failed";
            }

            sendHistoryNotification(message);

            if (numberOfStocks == 0) {
                int rowsCountDeleteStock = statement.executeUpdate(String.format("DELETE FROM SELLS WHERE id_stock=%s", stock.getId()));

                if (rowsCountDeleteStock == 1) {
                    message = String.format("Stock sell with id %s was completed");
                } else {
                    message = "Stock sell offer wasn't completed";
                }
            }

            statement.close();
            sendHistoryNotification(message);
        } catch (InterruptedException | IOException | TimeoutException e) {
            System.out.println("Blocked access");
        } finally {
            mutex.release();
        }
    }

    private void sendHistoryNotification(String message) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();

        try (Connection rabbitmqConnection = factory.newConnection()) {
            Channel channel = rabbitmqConnection.createChannel();
            channel.queueDeclare("hello", false, false, false, null);

            channel.basicPublish("", "hello", false, null, message.getBytes());
        }
    }
}
