package processes;

import bursa.SqliteDB;
import models.Bid;
import models.Buyer;
import models.Stock;

import java.sql.SQLException;
import java.util.List;

public class BuyerProcess extends Thread {

    private Buyer buyer;
    private SqliteDB sqliteDB;

    public BuyerProcess(Buyer buyer, SqliteDB sqliteDB) {
        this.buyer = buyer;
        this.sqliteDB = sqliteDB;
    }

    public void run() {
        long t = System.currentTimeMillis();
        long end = t + 5000;
        while (System.currentTimeMillis() < end) {
            try {
                List<Stock> stocks = sqliteDB.getStocks();
                for (Bid bid : buyer.getBidList()) {
                    for (Stock stock : stocks) {
                        if (bid.getStockId().equals(stock.getId())) {
                            if (stock.getPrice() - bid.getPrice() <= 2 && bid.getPrice() > stock.getPrice()) {
                                sqliteDB.exchange(stock, bid);
                            } else {
                                sqliteDB.updateBid(bid);
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