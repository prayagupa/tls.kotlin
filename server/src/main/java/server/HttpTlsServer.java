package server;
/**
 * Created by prayagupd
 * on 11/14/16.
 */

import java.io.FileInputStream;
import java.security.KeyStore;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class HttpTlsServer {
    private int port;
    private boolean isServerDone = false;

    HttpTlsServer(int port) {
        this.port = port;
    }

    public void run() {
        SSLContext sslContext = Keystore.createTLSContext();

        try {
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(this.port);

            System.out.println("TLS server started");
            while (!isServerDone) {
                SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
                new ServerThread(sslSocket).start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Create the and initialize the SSLContext
}
