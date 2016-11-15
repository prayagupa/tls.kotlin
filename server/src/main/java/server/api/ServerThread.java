package server.api;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.*;

/**
 * Created by prayagupd
 * on 11/14/16.
 */

// Thread handling the socket from client
public class ServerThread extends Thread {

    private SSLSocket sslSocket = null;


    ServerThread(SSLSocket sslSocket) {
        this.sslSocket = sslSocket;
    }


    public void run() {
        sslSocket.setEnabledCipherSuites(sslSocket.getSupportedCipherSuites());

        try {
// Start handshake
            sslSocket.startHandshake();

// Get session after the connection is established
            SSLSession sslSession = sslSocket.getSession();

            System.out.println("SSLSession :");
            System.out.println("\tProtocol : " + sslSession.getProtocol());
            System.out.println("\tCipher suite : " + sslSession.getCipherSuite());

// Start handling application content
            InputStream receivingStream = sslSocket.getInputStream();
            OutputStream sendStream = sslSocket.getOutputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(receivingStream));
            PrintWriter response = new PrintWriter(new OutputStreamWriter(sendStream));

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println("Server consumes : " + line);

                if (line.trim().isEmpty()) {
                    break;
                }
            }

            // Write data
            response.print("HTTP/1.1 200\r\n");
            response.flush();

            sslSocket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
