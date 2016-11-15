package server.api

import java.io.FileInputStream
import java.security.KeyStore
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

/**
 * Created by prayagupd
 * on 11/14/16.
 */

class Keystore {

    companion object {

        fun createTLSContext(): SSLContext? {
            try {
                val keyStore: KeyStore = KeyStore.getInstance("JKS")
                keyStore.load(FileInputStream("conf/eccountKeyStore.jks"), "eccount".toCharArray())

                // Create key manager
                val keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
                keyManagerFactory.init(keyStore, "eccount".toCharArray())
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
}
