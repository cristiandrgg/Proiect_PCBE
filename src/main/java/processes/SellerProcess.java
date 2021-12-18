package processes;

import bursa.SqliteDB;
import models.Bid;
import models.Seller;
import models.Stock;

import java.sql.SQLException;
import java.util.List;

public class SellerProcess extends Thread {

    private Seller seller;
    SqliteDB sqliteDB = new SqliteDB();
    boolean semaphore = false;
    boolean modify = false;

    public SellerProcess(Seller seller) {
        this.seller = seller;
    }

    public void run() {
        long t= System.currentTimeMillis();
        long end = t+5000;
        while (System.currentTimeMillis() < end) {
            try {
                List<Bid> bids = sqliteDB.getBids();
                for (Stock stock : seller.getStockList()) {
                    System.out.printf("Seller with id %s sells stock with id %s%n", seller.getId(), stock.getId());
                    for (Bid bid : bids) {
                        if (bid.getStockId().equals(stock.getId())) {
                            if (bid.getPrice() == stock.getPrice()) {
                                // TODO
                            } else {
                                modify = false;
                                if (bid.getPrice() > stock.getPrice()) {
                                    modify = true;
                                }
                                sqliteDB.updateStock(stock, modify);
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
