package client.api

import client.api.tls.ClientTruststore
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

/**
 * HTTP/2 client over TLS 1.3 using java.net.http.HttpClient (Java 11+).
 *
 * HTTP/2 is negotiated automatically via ALPN during the TLS handshake.
 * The custom SSLContext (loaded from the TrustStore) is injected directly,
 * so our pinned Root CA is the only trusted issuer — no system CA leakage.
 */
class HttpTlsClient(
    private val host: String,
    private val port: Int,
    val keyStoreFile: String,
    val trustStoreType: String,
    val password: String,
    val tlsVersion: String
) {
    var mMessage = ""

    fun start() {
        val sslContext = ClientTruststore.createTLSContext(keyStoreFile, trustStoreType, password, tlsVersion)!!

        val client = HttpClient.newBuilder()
            .sslContext(sslContext)                     // inject our TrustStore-backed SSLContext
            .version(HttpClient.Version.HTTP_2)         // prefer HTTP/2; falls back to HTTP/1.1 if server doesn't support it
            .build()

        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://$host:$port/"))
            .header("X-Message", mMessage)
            .GET()
            .build()

        println("[INFO] HttpTlsClient HTTP/2 over TLS 1.3 started")

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        println("[INFO] ClientConnection HTTP version  : ${response.version()}")
        println("[INFO] ClientConnection status        : ${response.statusCode()}")
        println("[INFO] ClientConnection body          : ${response.body()}")
    }
}
