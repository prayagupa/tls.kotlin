create keystore with public/private keys
------------------------------------------

```bash
prayagupd at prayagupd-vbox in /media/sf_programming/https-server-kotlin/src
$ mkdir conf
```

```
$ keytool -genkey -alias eccount -keyalg RSA -keystore conf/eccountKeyStore.jks
Enter keystore password:  
Re-enter new password: 
What is your first and last name?
  [Unknown]:  Prayag
What is the name of your organizational unit?
  [Unknown]:  dvnhlsm
What is the name of your organization?
  [Unknown]:  dvnhlsm
What is the name of your City or Locality?
  [Unknown]:  SEA
What is the name of your State or Province?
  [Unknown]:  WA
What is the two-letter country code for this unit?
  [Unknown]:  US
Is CN=Prayag, OU=dvnhlsm, O=dvnhlsm, L=SEA, ST=WA, C=US correct?
  [no]:  Yes

Enter key password for <eccount>
	(RETURN if same as keystore password): 
```

Sending requests
----------------

send HTTP request without trust-store loaded, to non-secure socket endpoint 

```
$ curl -v -XGET localhost:9999
* Rebuilt URL to: localhost:9999/
*   Trying ::1...
* Connected to localhost (::1) port 9999 (#0)
> GET / HTTP/1.1
> Host: localhost:9999
> User-Agent: curl/7.43.0
> Accept: */*
> 

* Connection #0 to host localhost left intact

// throws

javax.net.ssl.SSLException: Unrecognized SSL message, plaintext connection?
	at sun.security.ssl.InputRecord.handleUnknownRecord(InputRecord.java:710)
	at sun.security.ssl.InputRecord.read(InputRecord.java:527)
	at sun.security.ssl.SSLSocketImpl.readRecord(SSLSocketImpl.java:973)
	at sun.security.ssl.SSLSocketImpl.performInitialHandshake(SSLSocketImpl.java:1375)
	at sun.security.ssl.SSLSocketImpl.startHandshake(SSLSocketImpl.java:1403)
	at sun.security.ssl.SSLSocketImpl.startHandshake(SSLSocketImpl.java:1387)
	at server.api.tls.NonBlockingSecuredServerProcessor.run(NonBlockingSecuredServerProcessor.java:26)
```

send HTTP request without trust-store loaded to secured endpoint

```
$ curl -v -XGET https://localhost:9999
* Rebuilt URL to: https://localhost:9999/
*   Trying ::1...
* Connected to localhost (::1) port 9999 (#0)
* SSL certificate problem: Invalid certificate chain
* Closing connection 0
curl: (60) SSL certificate problem: Invalid certificate chain
More details here: http://curl.haxx.se/docs/sslcerts.html

curl performs SSL certificate verification by default, using a "bundle"
 of Certificate Authority (CA) public keys (CA certs). If the default
 bundle file isn't adequate, you can specify an alternate file
 using the --cacert option.
If this HTTPS server uses a certificate signed by a CA represented in
 the bundle, the certificate verification probably failed due to a
 problem with the certificate (it might be expired, or the name might
 not match the domain name in the URL).
If you'd like to turn off curl's verification of the certificate, use
 the -k (or --insecure) option.


//application error

javax.net.ssl.SSLHandshakeException: Received fatal alert: certificate_unknown
	at sun.security.ssl.Alerts.getSSLException(Alerts.java:192)
	at sun.security.ssl.Alerts.getSSLException(Alerts.java:154)
	at sun.security.ssl.SSLSocketImpl.recvAlert(SSLSocketImpl.java:2023)
	at sun.security.ssl.SSLSocketImpl.readRecord(SSLSocketImpl.java:1125)
	at sun.security.ssl.SSLSocketImpl.performInitialHandshake(SSLSocketImpl.java:1375)
	at sun.security.ssl.SSLSocketImpl.startHandshake(SSLSocketImpl.java:1403)
	at sun.security.ssl.SSLSocketImpl.startHandshake(SSLSocketImpl.java:1387)
	at server.api.tls.NonBlockingSecuredServerProcessor.run(NonBlockingSecuredServerProcessor.java:26)
```


```

$ curl -v -XGET --cert conf/eccountKeyStore.jks:eccount https://localhost:9999
* Rebuilt URL to: https://localhost:9999/
*   Trying ::1...
* Connected to localhost (::1) port 9999 (#0)
* WARNING: SSL: Certificate type not set, assuming PKCS#12 format.
* SSL: Couldn't make sense of the data in the certificate "conf/eccountKeyStore.jks" and its private key.
* Closing connection 0
curl: (58) SSL: Couldn't make sense of the data in the certificate "conf/eccountKeyStore.jks" and its private key.


// application error
javax.net.ssl.SSLHandshakeException: Remote host closed connection during handshake
	at sun.security.ssl.SSLSocketImpl.readRecord(SSLSocketImpl.java:992)
	at sun.security.ssl.SSLSocketImpl.performInitialHandshake(SSLSocketImpl.java:1375)
	at sun.security.ssl.SSLSocketImpl.startHandshake(SSLSocketImpl.java:1403)
	at sun.security.ssl.SSLSocketImpl.startHandshake(SSLSocketImpl.java:1387)
	at server.api.tls.NonBlockingSecuredServerProcessor.run(NonBlockingSecuredServerProcessor.java:26)
Caused by: java.io.EOFException: SSL peer shut down incorrectly
	at sun.security.ssl.InputRecord.read(InputRecord.java:505)
	at sun.security.ssl.SSLSocketImpl.readRecord(SSLSocketImpl.java:973)
	... 4 more

```