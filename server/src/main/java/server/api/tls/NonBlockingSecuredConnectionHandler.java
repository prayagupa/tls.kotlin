package server.api.tls;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.*;

/**
 * Created by prayagupd
 * on 11/14/16.
 */

// Thread handling the socket from client
public class NonBlockingSecuredConnectionHandler extends Thread {

    private SSLSocket securedSocket = null;

    public NonBlockingSecuredConnectionHandler(SSLSocket securedSocket) {
        this.securedSocket = securedSocket;
    }

    public void run() {
        securedSocket.setEnabledCipherSuites(securedSocket.getSupportedCipherSuites());

        try {
            // Start handshake
            securedSocket.startHandshake();

            // Get session after the connection is established
            SSLSession tlsSession = securedSocket.getSession();

            System.out.println("[INFO] NonBlockingSecuredConnectionHandler TLSSession :");
            System.out.println("\tProtocol : " + tlsSession.getProtocol());
            System.out.println("\tCipher suite : " + tlsSession.getCipherSuite());

            // Start handling application content
            InputStream receivingStream = securedSocket.getInputStream();
            OutputStream sendStream = securedSocket.getOutputStream();

            BufferedReader receivingBuffer = new BufferedReader(new InputStreamReader(receivingStream));
            PrintWriter response = new PrintWriter(new OutputStreamWriter(sendStream));

            String line = null;
            while ((line = receivingBuffer.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    break;
                }
                System.out.println("[INFO] NonBlockingSecuredConnectionHandler Server consumes : " + line);
            }

            // Write data to sending response
            response.print("HTTP/1.1 200\r\n");
            response.flush();

            securedSocket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            PrintWriter printWriter = new PrintWriter(new StringWriter());
            printWriter.println("HTTP/1.1 401\r\n");
            printWriter.flush();
        }
    }
}
