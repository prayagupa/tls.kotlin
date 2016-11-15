package client;

/**
 * Created by prayagupd on 11/14/16.
 */
public class Client {

    public static void main(String[] args) {
        HttpTlsClient client = new HttpTlsClient("127.0.0.1", 9999);
        //client.message("client sends love");
        client.run();
    }
}
