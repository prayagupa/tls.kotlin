package client.api.tls

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import javax.net.ssl.SSLSocket

/**
 * Created by prayagupd
 * on 11/14/16.
 */

// Thread handling the socket to server
class ClientConnectionThread(var tlsV1Socket: SSLSocket, val message: String) : Thread() {

    override fun run() {
        tlsV1Socket.enabledCipherSuites = tlsV1Socket.supportedCipherSuites

        try {
            // Start handshake
            tlsV1Socket.startHandshake()

            // Get session after the connection is established
            val tlsV1Session = tlsV1Socket.session

            println("[INFO] ClientConnectionThread SSLSession :")
            println("\tProtocol : " + tlsV1Session.protocol)
            println("\tCipher suite : " + tlsV1Session.cipherSuite)

            // Start handling application content
            val socketSendChannel = tlsV1Socket.outputStream
            val httpRequestToSendChannel = PrintWriter(OutputStreamWriter(socketSendChannel))
            // Write data
            httpRequestToSendChannel.println(message)
            httpRequestToSendChannel.println()
            httpRequestToSendChannel.flush()

            val receiveChannel = tlsV1Socket.inputStream
            val receiveChannelReader = BufferedReader(InputStreamReader(receiveChannel))

            var response = receiveChannelReader.readLine()
            while (response != null) {
                println("[INFO] ClientConnectionThread received : " + response)

                if (response.trim { it <= ' ' } == "HTTP/1.1 200\r\n") {
                    break
                }
                response = receiveChannelReader.readLine()
            }

            tlsV1Socket.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }
}
