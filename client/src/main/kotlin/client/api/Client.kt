package client.api

import java.util.*

/**
 * Created by prayagupd
 * on 11/14/16.
 */

object Client {

    @JvmStatic fun main(args: Array<String>) {

        val clientTrustStoreFile = "../server/conf3/restapi-server.p12"
        val clientPassword = "server"

        val client = HttpTlsClient("127.0.0.1", 2810, clientTrustStoreFile, "pkcs12", clientPassword,
                "TLSv1")

        client.mMessage = "client sends love at ${Date().time}" //TODO bad design

        client.start()
    }
}
