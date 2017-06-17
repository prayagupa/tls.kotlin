package server.api

/**
 * Created by prayagupd
 * on 11/15/16.
 */

object Server {

    @JvmStatic fun main(args: Array<String>) {

        val password = "restapi-password"
        val keyStoreFile = "conf/restapi.jks"

        val server = HttpTlsServer(9999, password, keyStoreFile)
        server.start()
    }

}
