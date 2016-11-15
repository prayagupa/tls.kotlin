package client; /**
 * Created by prayagupd
 * on 11/14/16.
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.KeyStore;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class HttpTlsClient {
    private String host;
    private int port;

    HttpTlsClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    // Start to run the server
    public void run() {
        SSLContext sslContext = Keystore.createTLSContext();
        try {
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(this.host, this.port);

            System.out.println("TLS client started");
            new ClientThread(sslSocket).start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}