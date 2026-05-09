package client.api

import client.api.tls.ClientTruststore
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import javax.net.ssl.SSLSocket

/**
 * Created by prayagupd
 * on 11/15/16.
 */

class HttpTlsClient(private val host: String, private val port: Int, val keyStoreFile: String,
                    val trustStoreType: String, val password: String, val tlsVersion: String) {

    var mMessage = ""

    // Start to run the server
    fun start() {
        val tlSecuredContext = ClientTruststore.createTLSContext(keyStoreFile, trustStoreType, password, tlsVersion)
        try {
            val tlsSocketFactory = tlSecuredContext!!.socketFactory
            val remoteSecuredSocket = tlsSocketFactory.createSocket(this.host, this.port) as SSLSocket

            println("[INFO] HttpTlsClient TLSv1 client started\n")
            ClientConnectionThread(remoteSecuredSocket, mMessage).start()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}


// Thread handling the socket to server
class ClientConnectionThread(var tlsV1Socket: SSLSocket, val message: String) : Thread() {

    override fun run() {
        // Restrict to TLS 1.3 only — disable TLS 1.0/1.1/1.2
        tlsV1Socket.enabledProtocols = arrayOf("TLSv1.3")
        // Pin to the three mandatory TLS 1.3 cipher suites (RFC 8446 §B.4)
        tlsV1Socket.enabledCipherSuites = arrayOf(
            "TLS_AES_256_GCM_SHA384",
            "TLS_CHACHA20_POLY1305_SHA256",
            "TLS_AES_128_GCM_SHA256"
        )

        try {
            // Start handshake
            tlsV1Socket.startHandshake()

            // Get session after the connection is established
            val tlsV1Session = tlsV1Socket.session

            println("[INFO] ClientConnection TLSSession :")
            println("\tTLS Protocol : " + tlsV1Session.protocol)
            println("\tTLS Cipher suite : " + tlsV1Session.cipherSuite)

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
                println("[INFO] ClientConnection received : " + response)

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
