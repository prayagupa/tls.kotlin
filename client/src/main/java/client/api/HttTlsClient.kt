package client.api

import client.api.whatever.ClientThread
import client.api.whatever.Keystore
import javax.net.ssl.SSLSocket

/**
 * Created by prayagupd
 * on 11/15/16.
 */

class HttpTlsClient(private val host: String, private val port: Int) {

    var message = ""

    // Start to run the server
    fun run() {
        val sslContext = Keystore.createTLSContext()
        try {
            val sslSocketFactory = sslContext!!.socketFactory
            val sslSocket = sslSocketFactory.createSocket(this.host, this.port) as SSLSocket

            println("TLS client started")
            ClientThread(sslSocket, message).start()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}