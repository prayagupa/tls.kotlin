package client.api;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

/**
 * Created by prayagupd
 * on 11/14/16.
 */

// Thread handling the socket to server
public class ClientThread extends Thread {

    private SSLSocket sslSocket = null;
    private String message = "";

    ClientThread(SSLSocket sslSocket, String message) {
        this.sslSocket = sslSocket;
        this.message = message;
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

            OutputStream sendChannel = sslSocket.getOutputStream();
            PrintWriter httpRequestToSendChannel = new PrintWriter(new OutputStreamWriter(sendChannel));
            // Write data
            httpRequestToSendChannel.println(message);
            httpRequestToSendChannel.println();
            httpRequestToSendChannel.flush();

            InputStream receiveChannel = sslSocket.getInputStream();
            BufferedReader receiveChannelReader = new BufferedReader(new InputStreamReader(receiveChannel));

            String response = null;
            while ((response = receiveChannelReader.readLine()) != null) {
                System.out.println("Input : " + response);

                if (response.trim().equals("HTTP/1.1 200\r\n")) {
                    break;
                }
            }

            sslSocket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
