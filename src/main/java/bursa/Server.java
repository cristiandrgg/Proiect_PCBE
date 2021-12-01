package bursa;

import models.Bid;
import models.Buyer;
import models.Seller;
import models.Stock;
import processes.BuyerProcess;
import processes.SellerProcess;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by: Doru
 * Date: 09/11/2021
 */

public class Server {

    public static void main(String[] args) throws SQLException {
        SqliteDB sqliteDB = new SqliteDB();

        System.out.println("Welcome to Timisoara Stock Exchange");

        List<Seller> sellers = sqliteDB.getSellers();
        List<Buyer> buyers = sqliteDB.getBuyers();
        List<Stock> stocks = sqliteDB.getStocks();
        List<Bid> bids = sqliteDB.getBids();

        print(sellers, buyers, bids, stocks);

        for(Seller seller : sellers){
            SellerProcess sellerProcess = new SellerProcess(seller);
            sellerProcess.start();
        }
        for(Buyer buyer : buyers){
            BuyerProcess buyerProcess = new BuyerProcess(buyer);
            buyerProcess.start();
        }
    }

    private static void print(List<Seller> sellers, List<Buyer> buyers, List<Bid> bids, List<Stock> stocks) {
        System.out.println("Buyers:");
        for (Buyer buyer : buyers) {
            System.out.println(buyer.getId() + " " + buyer.getName());

            for (Bid bid : buyer.getBidList()) {
                System.out.println(bid.getId() + " " + bid.getStockId() + " " + bid.getPrice() + " " + bid.getNumberOfStocks());
            }
            System.out.println("\n");
        }

        System.out.println("\nSellers:");
        for (Seller seller : sellers) {
            System.out.println(seller.getId() + " " + seller.getName());

            for (Stock stock : seller.getStockList()) {
                System.out.println(stock.getId() + " " + stock.getName() + " " + stock.getNumberOfStocks() + " " + stock.getSellerId());
            }

            System.out.println("\n");
        }

        System.out.println("\nBids:");
        for (Bid bid : bids) {
            System.out.println(bid.getId() + " " + bid.getBuyerId() + " " + bid.getStockId() + " " + bid.getPrice() + " " + bid.getNumberOfStocks());
        }

        System.out.println("\nStocks:");
        for (Stock stock : stocks) {
            System.out.println(stock.getId() + " " + stock.getName() + " " + stock.getNumberOfStocks() + " " + stock.getPrice() + " " + stock.getSellerId());
        }
    }
}
