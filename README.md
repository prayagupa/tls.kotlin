TLS server-client communication using Public/Private Key
--------------------------------------------------------

```
client loaded with Server Certificates            |          Server
--------------------------------------------------|------------------------------------
Message --> [Public Key]-->Encrypted Message------|----->  [Private Key]-->Message
                 |                                |             |
                 |                                |             |
                 V                                |             V
             TrustStore                           |          Keystore

```

The trick in a `key pair` is to keep one key secret `(the private key)` and to distribute the 
other key `(the public key)` to everybody. 
Anybody can send an encrypted message to the SERVER, that only SERVER will be able to decrypt.

[1.2. What is TLS and what are Certificates?](http://tldp.org/HOWTO/SSL-Certificates-HOWTO/x64.html)

[Public Keys, Private Keys, and Certificates](https://docs.oracle.com/cd/E19509-01/820-3503/ggbgc/index.html)

![](https://docs.oracle.com/cd/E19509-01/820-3503/images/encryption-and-decryption.gif)


How does client know that it is dealing with the right person or rather the right web server. 
---------------------------------------------------------------------------------------------

This right person/web server, client has to implicitly trust: 
client has his/SERVER's certificate loaded in its browser/application (a root Certificate). 


[Trust Store vs Key Store - creating with keytool](http://stackoverflow.com/a/6341566/432903)

[Truststore and Keystore Definitions](http://stackoverflow.com/a/18912385/432903)

```
Keystore contains private keys, and the certificates with their corresponding public keys.

Essentially, javax.net.ssl.keyStore is meant to contain your private keys and certificates, 
```


```
A Truststore contains certificates from other parties that you expect to communicate with, 
or from CAs(Certificate Authorities) that you trust to identify other parties.

Also, javax.net.ssl.trustStore is meant to contain the CA certificates 
you're willing to trust when a remote party presents its certificate.
```

create a keystore - 


refs
-----

https://en.wikipedia.org/wiki/Transport_Layer_Security#Certificate_authorities

https://dev.twitter.com/overview/api/tls

