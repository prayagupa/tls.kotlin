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

class CertificateStore {

    companion object {

        fun createTLSContext(keyStoreFile: String, keystoreType: String, password: String, tlsVersion: String): SSLContext? {
            try {
                val keyStoreLoader: KeyStore = KeyStore.getInstance(keystoreType)
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
                val tlSecurityContext = SSLContext.getInstance(tlsVersion)
                tlSecurityContext.init(km, tm, null)

                return tlSecurityContext
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            return null
        }
    }
}
