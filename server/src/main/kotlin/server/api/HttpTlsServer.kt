package server.api

import server.api.tls.CertificateStore
import java.io.*
import java.util.concurrent.atomic.AtomicBoolean
import javax.net.ssl.SSLServerSocket
import javax.net.ssl.SSLSocket

/**
 * Created by prayagupd
 * on 11/15/16.
 */

class HttpTlsServer(private val port: Int, val keyStoreFile: String, private val password: String,
                    private val certType: String, val tlsVersion: String) {
    private var stopListeningOnSecuredSocket = AtomicBoolean(false)

    fun start() {
        val tlSecurityContext = CertificateStore.createTLSContext(keyStoreFile, certType, password, tlsVersion)

        try {
            val tlsSecuredServerSocketFactory = tlSecurityContext!!.serverSocketFactory
            val tlSecuredServerSocket = tlsSecuredServerSocketFactory.createServerSocket(this.port) as SSLServerSocket

            // Restrict to TLS 1.3 only — disable TLS 1.0/1.1/1.2
            tlSecuredServerSocket.enabledProtocols = arrayOf("TLSv1.3")
            // Pin to the three mandatory TLS 1.3 cipher suites (RFC 8446 §B.4)
            tlSecuredServerSocket.enabledCipherSuites = arrayOf(
                "TLS_AES_256_GCM_SHA384",
                "TLS_CHACHA20_POLY1305_SHA256",
                "TLS_AES_128_GCM_SHA256"
            )

            println("[INFO] HttpTlsServer TLSv1.3 server started!!!")

            while (!stopListeningOnSecuredSocket.get()) {
                //handshake per connection
                val connection = tlSecuredServerSocket.accept() as SSLSocket
                NonBlockingSecuredConnectionHandler(connection).start()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun stop() {
        stopListeningOnSecuredSocket = AtomicBoolean(true)
    }
}


// Thread handling the socket from client
class NonBlockingSecuredConnectionHandler(var securedSocket: SSLSocket) : Thread() {

    override fun run() {
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
