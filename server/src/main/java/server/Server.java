package server;

/**
 * Created by prayagupd
 * on 11/14/16.
 */

public class Server {

    public static void main(String[] args) {
        HttpTlsServer server = new HttpTlsServer(9999);
        server.run();
    }

}
