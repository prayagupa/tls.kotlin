package server.api

/**
 * Created by prayagupd
 * on 11/15/16.
 */

enum class CertTypes constructor (val certType: String) {
    JavaKeyStore("JKS"),
    PKCS12("pkcs12")
}

object Server {

    @JvmStatic fun main(args: Array<String>) {

        val cert = "conf3/restapi-server.p12"
        val certPassword = "server"

        val server = HttpTlsServer(2810, cert, certPassword, CertTypes.PKCS12.certType, "TLSv1")
        server.start()
    }

}
