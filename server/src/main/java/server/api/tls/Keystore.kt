package server.api.tls

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

        fun createTLSContext(keyStoreFile: String, password: String): SSLContext? {
            try {
                val keyStoreLoader: KeyStore = KeyStore.getInstance("JKS")
                keyStoreLoader.load(FileInputStream(keyStoreFile), password.toCharArray())

                // Create key manager
                val keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
                keyManagerFactory.init(keyStoreLoader, password.toCharArray())
                val km = keyManagerFactory.keyManagers

                // Create trust manager
                val trustManagerFactory = TrustManagerFactory.getInstance("SunX509")
                trustManagerFactory.init(keyStoreLoader)
                val tm = trustManagerFactory.trustManagers

                // Initialize TLSContext
                val tlSecurityContext = SSLContext.getInstance("TLSv1")
                tlSecurityContext.init(km, tm, null)

                return tlSecurityContext
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            return null
        }
    }
}
