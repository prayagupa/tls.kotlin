package server.api.tls

import java.io.*
import javax.net.ssl.SSLSocket

/**
 * Created by prayagupd
 * on 11/14/16.
 */

// Thread handling the socket from client
class NonBlockingSecuredConnectionHandler(var securedSocket: SSLSocket) : Thread() {

    override fun run() {
        val supportedCipherSuites = securedSocket.supportedCipherSuites

        securedSocket.enabledCipherSuites = supportedCipherSuites

        try {
            // Start handshake
            securedSocket.startHandshake()

            // Get session after the connection is established
            val tlsSession = securedSocket.session

            println("[INFO] NonBlockingSecuredConnectionHandler TLSSession :")
            println("\tProtocol : " + tlsSession?.protocol)
            println("\tCipher suite : " + tlsSession?.cipherSuite)

            // Start handling application content
            val receivingStream = securedSocket.inputStream
            val sendStream = securedSocket.outputStream

            val receivingBuffer = BufferedReader(InputStreamReader(receivingStream))
            val response = PrintWriter(OutputStreamWriter(sendStream))

            var line = receivingBuffer.readLine()
            while (line!= null) {
                if (line.trim { it <= ' ' }.isEmpty()) {
                    break
                }
                println("[INFO] NonBlockingSecuredConnectionHandler Server consumes : " + line)
                line = receivingBuffer.readLine()
            }

            // Write data to sending response
            response.print("HTTP/1.1 200\r\n")
            response.flush()

            securedSocket.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
            val printWriter = PrintWriter(StringWriter())
            printWriter.println("HTTP/1.1 401\r\n")
            printWriter.flush()
        }

    }
}
