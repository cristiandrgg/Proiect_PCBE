package threads;

import processes.BuyerProcess;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BuyerThread extends Thread {
    public void run() {
        Socket socket;
        try {
            ServerSocket Server = new ServerSocket(4000);
            while (true) {
                socket = Server.accept();
                new BuyerProcess(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}