package client.api

import java.util.*

/**
 * Created by prayagupd
 * on 11/14/16.
 */

object Client {

    @JvmStatic fun main(args: Array<String>) {

        val clientTrustStoreFile = "conf/restapi.jks"
        val clientPassword = "restapi-pass"

        val client = HttpTlsClient("127.0.0.1", 2810, clientTrustStoreFile, "JKS", clientPassword,
                "TLSv1")

        client.mMessage = "client sends love at ${Date().time}" //TODO bad design

        client.start()
    }
}
