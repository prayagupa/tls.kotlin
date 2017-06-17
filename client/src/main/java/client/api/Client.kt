package client.api

/**
 * Created by prayagupd
 * on 11/14/16.
 */

object Client {

    @JvmStatic fun main(args: Array<String>) {

        val keyStoreFile = "conf/restapi.jks"
        val password = "restapi-pass"

        val client = HttpTlsClient("127.0.0.1", 9999, keyStoreFile, password)

        client.message = "client sends love"
        client.start()
    }
}
