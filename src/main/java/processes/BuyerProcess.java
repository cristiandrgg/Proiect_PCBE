package processes;

import java.net.Socket;

public class BuyerProcess extends Thread {

    private Socket socket;

    public BuyerProcess(Socket socket) {
        this.socket = socket;
    }
}