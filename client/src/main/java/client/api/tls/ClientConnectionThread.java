package client.api.tls;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Created by prayagupd
 * on 11/14/16.
 */

// Thread handling the socket to server
public class ClientConnectionThread extends Thread {

    private SSLSocket tlsSocket = null;
    private String message = "";

    public ClientConnectionThread(SSLSocket tlsV1Socket, String message) {
        this.tlsSocket = tlsV1Socket;
        this.message = message;
    }

    public void run() {
        tlsSocket.setEnabledCipherSuites(tlsSocket.getSupportedCipherSuites());

        try {
            // Start handshake
            tlsSocket.startHandshake();

            // Get session after the connection is established
            SSLSession tlsV1Session = tlsSocket.getSession();

            System.out.println("[INFO] ClientConnectionThread SSLSession :");
            System.out.println("\tProtocol : " + tlsV1Session.getProtocol());
            System.out.println("\tCipher suite : " + tlsV1Session.getCipherSuite());

            // Start handling application content
            OutputStream socketSendChannel = tlsSocket.getOutputStream();
            PrintWriter httpRequestToSendChannel = new PrintWriter(new OutputStreamWriter(socketSendChannel));
            // Write data
            httpRequestToSendChannel.println(message);
            httpRequestToSendChannel.println();
            httpRequestToSendChannel.flush();

            InputStream receiveChannel = tlsSocket.getInputStream();
            BufferedReader receiveChannelReader = new BufferedReader(new InputStreamReader(receiveChannel));

            String response = null;
            while ((response = receiveChannelReader.readLine()) != null) {
                System.out.println("[INFO] ClientConnectionThread received : " + response);

                if (response.trim().equals("HTTP/1.1 200\r\n")) {
                    break;
                }
            }

            tlsSocket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
