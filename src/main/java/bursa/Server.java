package bursa;

import models.Bid;
import models.Buyer;
import models.Seller;
import models.Stock;
import processes.BuyerProcess;
import processes.HistoryProcess;
import processes.SellerProcess;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Server {

    public static void main(String[] args) throws SQLException, InterruptedException {

        System.out.println("Welcome to Timisoara Stock Exchange");

        List<Seller> sellers = null;
        List<Buyer> buyers = null;
        List<Stock> stocks;
        List<Bid> bids;
        SqliteDB sqliteDB = new SqliteDB();

        while (true) {
            System.out.println("\nLoad DataBase - 1\n" +
                    "Start Simulation - 2\n" +
                    "Exit Application - Anything Else\n" +
                    "\n" +
                    "Pick an option: ");

            Scanner scanner = new Scanner(System.in);
            String option = scanner.nextLine();

            if (option.equals("1")) {
                System.out.println("DataBase is loading!");

                sellers = sqliteDB.getSellers();
                buyers = sqliteDB.getBuyers();
                stocks = sqliteDB.getStocks();
                bids = sqliteDB.getBids();
                System.out.println("DataBase loaded!");
                print(sellers, buyers, bids, stocks);
            } else if (option.equals("2")) {
                ArrayList<Thread> childThreads = new ArrayList<>();
                System.out.println("Starting simulation");
                if (sellers == null || buyers == null) {
                    System.out.println("WARNING: Simulation failed -> sellers or buyers are not loaded. Please load DataBase!");
                    continue;
                }
                for (Seller seller : sellers) {
                    SellerProcess sellerProcess = new SellerProcess(seller, sqliteDB);
                    Thread t = new Thread(sellerProcess);
                    childThreads.add(t);
                    t.start();
                }
                for (Buyer buyer : buyers) {
                    BuyerProcess buyerProcess = new BuyerProcess(buyer, sqliteDB);
                    Thread t = new Thread(buyerProcess);
                    childThreads.add(t);
                    t.start();
                }
                Thread t = new Thread(new HistoryProcess());
                t.start();
                for (Thread thread : childThreads) {
                    thread.join();
                }
                System.out.println("Simulation ended");
            } else {
                System.out.println("Thank you for using Timisoara Stock Exchange and have a nice day!");
                break;
            }
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
