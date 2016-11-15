package server.api

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