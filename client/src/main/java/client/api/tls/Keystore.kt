package client.api.tls

import java.io.FileInputStream
import java.security.KeyStore
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

/**
 * Created by prayagupd
 * on 11/15/16.
 */

object Keystore {

    fun createTLSContext(keyStoreFile: String, password: String): SSLContext? {
        try {
            val keyStore = KeyStore.getInstance("JKS")
            keyStore.load(FileInputStream(keyStoreFile), password.toCharArray())

            // Create key manager
            val keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
            keyManagerFactory.init(keyStore, password.toCharArray())
            val km = keyManagerFactory.keyManagers

            // Create trust manager
            val trustManagerFactory = TrustManagerFactory.getInstance("SunX509")
            trustManagerFactory.init(keyStore)
            val tm = trustManagerFactory.trustManagers

            // Initialize SSLContext
            val sslContext = SSLContext.getInstance("TLSv1")
            sslContext.init(km, tm, null)

            return sslContext
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return null
    }
}
