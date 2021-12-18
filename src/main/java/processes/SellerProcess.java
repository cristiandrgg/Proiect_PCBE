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

    public SellerProcess(Seller seller) {
        this.seller = seller;
    }

    public void run() {
        long t = System.currentTimeMillis();
        long end = t + 5000;
        while (System.currentTimeMillis() < end) {
            try {
                List<Bid> bids = sqliteDB.getBids();
                for (Stock stock : seller.getStockList()) {
                    for (Bid bid : bids) {
                        if (bid.getStockId().equals(stock.getId())) {
                            if (stock.getPrice() - bid.getPrice() <= 2 && bid.getPrice() > stock.getPrice()) {
                                sqliteDB.exchange(stock, bid);
                            } else {
                                sqliteDB.updateStock(stock);
                            }
                        }
                    }
                }
            } catch (SQLException | InterruptedException e) {
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