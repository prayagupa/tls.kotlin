package client.api

import client.api.tls.ClientConnectionThread
import client.api.tls.Keystore
import javax.net.ssl.SSLSocket

/**
 * Created by prayagupd
 * on 11/15/16.
 */

class HttpTlsClient(private val host: String, private val port: Int, val keyStoreFile: String, val password: String) {

    var message = ""

    // Start to run the server
    fun start() {
        val tlSecuredContext = Keystore.createTLSContext(keyStoreFile, password)
        try {
            val tlsSocketFactory = tlSecuredContext!!.socketFactory
            val remoteSecuredSocket = tlsSocketFactory.createSocket(this.host, this.port) as SSLSocket

            println("[INFO] HttpTlsClient TLSv1 client started")
            ClientConnectionThread(remoteSecuredSocket, message).start()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}