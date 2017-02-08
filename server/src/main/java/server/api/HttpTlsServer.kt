package server.api

import server.api.whatever.Keystore
import server.api.whatever.ServerThread
import javax.net.ssl.SSLServerSocket
import javax.net.ssl.SSLSocket

/**
 * Created by prayagupd
 * on 11/15/16.
 */

class HttpTlsServer(private val port: Int) {
    private val isServerDone = false

    fun run() {
        val tlsContext = Keystore.createTLSContext()

        try {
            val sslServerSocketFactory = tlsContext!!.serverSocketFactory
            val sslServerSocket = sslServerSocketFactory.createServerSocket(this.port) as SSLServerSocket

            println("TLS server started!!!")

            while (!isServerDone) {
                val sslSocket = sslServerSocket.accept() as SSLSocket
                ServerThread(sslSocket).start()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }
}