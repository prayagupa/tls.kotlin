package server.api

import server.api.HttpTlsServer

/**
 * Created by prayagupd
 * on 11/15/16.
 */

object Server {

    @JvmStatic fun main(args: Array<String>) {
        val server = HttpTlsServer(9999)
        server.run()
    }

}