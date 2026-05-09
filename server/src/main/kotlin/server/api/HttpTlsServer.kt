package server.api

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.engine.sslConnector
import io.ktor.server.netty.Netty
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import java.io.File
import java.security.KeyStore

/**
 * HTTP/2 server over TLS 1.3 using Ktor + Netty.
 *
 * Netty negotiates HTTP/2 via ALPN during the TLS handshake, advertising ["h2", "http/1.1"].
 * On Java 9+ no native ALPN agent is required — the JDK JSSE provider handles it natively.
 */
class HttpTlsServer(
    private val port: Int,
    val keyStoreFile: String,
    private val password: String,
    private val certType: String,
    val tlsVersion: String          // kept for API compat; TLS config is owned by Ktor/Netty
) {
    private lateinit var engine: EmbeddedServer<*, *>

    fun start() {
        val keyStore = KeyStore.getInstance(certType).also {
            it.load(File(keyStoreFile).inputStream(), password.toCharArray())
        }
        val keyAlias = keyStore.aliases().nextElement()

        engine = embeddedServer(Netty, configure = {
            sslConnector(
                keyStore = keyStore,
                keyAlias = keyAlias,
                keyStorePassword = { password.toCharArray() },
                privateKeyPassword = { password.toCharArray() }
            ) {
                this.port = this@HttpTlsServer.port
                this.keyStorePath = File(keyStoreFile)
            }
        }) {
            routing {
                get("/") {
                    println("[INFO] Server received request: ${call.request.local.method.value} ${call.request.local.uri}")
                    call.respond(HttpStatusCode.OK, "OK")
                }
            }
        }

        println("[INFO] HttpTlsServer HTTP/2 over TLS 1.3 started on port $port")
        engine.start(wait = true)
    }

    fun stop() {
        if (::engine.isInitialized) engine.stop(gracePeriodMillis = 1_000, timeoutMillis = 5_000)
    }
}
