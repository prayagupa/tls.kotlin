package server.api

import server.api.tls.Keystore
import server.api.tls.NonBlockingSecuredConnectionHandler
import java.util.concurrent.atomic.AtomicBoolean
import javax.net.ssl.SSLServerSocket
import javax.net.ssl.SSLSocket

/**
 * Created by prayagupd
 * on 11/15/16.
 */

class HttpTlsServer(private val port: Int, private val password: String, val keyStoreFile: String) {
    private var stopListeningOnSecuredSocket = AtomicBoolean(false)

    fun start() {
        val tlSecurityContext = Keystore.createTLSContext(keyStoreFile, password)

        try {
            val tlsSecuredServerSocketFactory = tlSecurityContext!!.serverSocketFactory
            val tlSecuredServerSocket = tlsSecuredServerSocketFactory.createServerSocket(this.port) as SSLServerSocket

            println("TLS server started!!!")

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
