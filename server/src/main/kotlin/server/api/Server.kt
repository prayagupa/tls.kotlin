package server.api

/**
 * Created by prayagupd
 * on 11/15/16.
 */

object Server {

    @JvmStatic fun main(args: Array<String>) {

        val keyStoreFile = "conf/restapi.jks"
        val password = "restapi-password"

        val server = HttpTlsServer(2810, password, keyStoreFile)
        server.start()
    }

}
