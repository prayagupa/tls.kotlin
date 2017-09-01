package server.api

/**
 * Created by prayagupd
 * on 11/15/16.
 */

object Server {

    @JvmStatic fun main(args: Array<String>) {

        val cert = "conf/restapi.jks"
        val certPassword = "restapi-password"

        val server = HttpTlsServer(2810, cert, certPassword,"JKS", "TLSv1")
        server.start()
    }

}
