package processes;

import java.net.Socket;

public class SellerProcess extends Thread {

    private Socket socket;
    private Integer i;

    public SellerProcess(Socket socket, Integer i) {
        this.socket = socket;
        this.i = i;
    }
}
