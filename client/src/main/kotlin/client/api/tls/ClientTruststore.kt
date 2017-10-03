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

object ClientTruststore {

    fun createTLSContext(trustStoreFile: String, trustStoreType: String, password: String, tlsVersion: String): SSLContext? {
        try {
            val keyStore = KeyStore.getInstance(trustStoreType)
            keyStore.load(FileInputStream(trustStoreFile), password.toCharArray())

            // Create key manager
            val keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
            keyManagerFactory.init(keyStore, password.toCharArray())
            val km = keyManagerFactory.keyManagers

            // Create trust manager
            val trustManagerFactory = TrustManagerFactory.getInstance("SunX509")
            trustManagerFactory.init(keyStore)
            val tm = trustManagerFactory.trustManagers

            // Initialize SSLContext
            val sslContext = SSLContext.getInstance(tlsVersion)
            sslContext.init(km, tm, null)

            return sslContext
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return null
    }
}
