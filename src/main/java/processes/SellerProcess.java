package processes;

import java.net.Socket;

public class SellerProcess extends Thread {

    private Socket socket;

    public SellerProcess(Socket socket) {
        this.socket = socket;
    }
}
