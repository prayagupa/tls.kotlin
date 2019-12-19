create truststore
-----------------

[Privacy Enhanced EMail](https://en.wikipedia.org/wiki/Privacy-enhanced_Electronic_Mail)

```
PEMail is a de facto file format for storing and sending cryptography keys, 
certificates, and other data, based on a set of 1993 IETF standards defining "privacy-enhanced mail."
```

```bash
$ keytool -printcert -file conf/restapi.pem 
keytool error: java.lang.Exception: Failed to parse input
```

A PEMail-encoded certificate:

```bash
$ openssl x509 -in conf/restapi.pem -text
Certificate:
    Data:
        Version: 3 (0x2)
        Serial Number: 1575226775 (0x5de40d97)
        Signature Algorithm: dsaWithSHA1
        Issuer: C=US, ST=WA, L=SEA, OU=dvnhlsm, O=com.restapi, CN=prayagupd
        Validity
            Not Before: Jun 17 12:13:46 2017 GMT
            Not After : Sep 15 12:13:46 2017 GMT
        Subject: C=US, ST=WA, L=SEA, OU=dvnhlsm, O=com.restapi, CN=prayagupd
        Subject Public Key Info:
            Public Key Algorithm: dsaEncryption
            DSA Public Key:
                pub: 
                    00:c3:48:f0:1a:54:45:2f:64:13:02:a3:0e:27:ce:
                    35:5c:6c:2c:58:b6:12:28:89:24:69:ca:7b:25:28:
                    6c:3e:ad:75:a6:fc:1b:6a:18:af:b0:61:ec:82:94:
                    40:f9:d6:b9:d8:46:35:25:71:19:b2:6a:dd:c2:ca:
                    4c:55:48:14:d2:ee:1c:17:96:b2:0c:99:20:4e:f6:
                    85:59:9e:01:3c:d0:ae:d6:eb:3c:e7:18:cb:7e:c1:
                    2b:5e:7f:15:61:c3:ba:73:5d:f2:29:a7:3b:86:9f:
                    6a:ca:43:73:83:2e:a9:ae:03:7c:d9:84:56:66:01:
                    db:d5:97:ea:53:f9:cd:55:c1
                P:   
                    00:fd:7f:53:81:1d:75:12:29:52:df:4a:9c:2e:ec:
                    e4:e7:f6:11:b7:52:3c:ef:44:00:c3:1e:3f:80:b6:
                    51:26:69:45:5d:40:22:51:fb:59:3d:8d:58:fa:bf:
                    c5:f5:ba:30:f6:cb:9b:55:6c:d7:81:3b:80:1d:34:
                    6f:f2:66:60:b7:6b:99:50:a5:a4:9f:9f:e8:04:7b:
                    10:22:c2:4f:bb:a9:d7:fe:b7:c6:1b:f8:3b:57:e7:
                    c6:a8:a6:15:0f:04:fb:83:f6:d3:c5:1e:c3:02:35:
                    54:13:5a:16:91:32:f6:75:f3:ae:2b:61:d7:2a:ef:
                    f2:22:03:19:9d:d1:48:01:c7
                Q:   
                    00:97:60:50:8f:15:23:0b:cc:b2:92:b9:82:a2:eb:
                    84:0b:f0:58:1c:f5
                G:   
                    00:f7:e1:a0:85:d6:9b:3d:de:cb:bc:ab:5c:36:b8:
                    57:b9:79:94:af:bb:fa:3a:ea:82:f9:57:4c:0b:3d:
                    07:82:67:51:59:57:8e:ba:d4:59:4f:e6:71:07:10:
                    81:80:b4:49:16:71:23:e8:4c:28:16:13:b7:cf:09:
                    32:8c:c8:a6:e1:3c:16:7a:8b:54:7c:8d:28:e0:a3:
                    ae:1e:2b:b3:a6:75:91:6e:a3:7f:0b:fa:21:35:62:
                    f1:fb:62:7a:01:24:3b:cc:a4:f1:be:a8:51:90:89:
                    a8:83:df:e1:5a:e5:9f:06:92:8b:66:5e:80:7b:55:
                    25:64:01:4c:3b:fe:cf:49:2a
        X509v3 extensions:
            X509v3 Subject Key Identifier: 
                60:5D:C5:84:C5:56:A4:B3:8A:AF:8A:73:C9:D5:46:1A:E7:AA:8F:F8
    Signature Algorithm: dsaWithSHA1
        30:2c:02:14:7c:49:fb:64:29:7c:48:16:f6:58:98:12:71:55:
        93:f9:fb:74:48:01:02:14:26:10:dc:a6:a2:e0:15:7d:87:3e:
        96:b7:58:87:1b:83:96:a1:46:e5
-----BEGIN CERTIFICATE-----
MIIDJTCCAuOgAwIBAgIEXeQNlzALBgcqhkjOOAQDBQAwZDELMAkGA1UEBhMCVVMx
CzAJBgNVBAgTAldBMQwwCgYDVQQHEwNTRUExEDAOBgNVBAsTB2R2bmhsc20xFDAS
BgNVBAoTC2NvbS5yZXN0YXBpMRIwEAYDVQQDEwlwcmF5YWd1cGQwHhcNMTcwNjE3
MTIxMzQ2WhcNMTcwOTE1MTIxMzQ2WjBkMQswCQYDVQQGEwJVUzELMAkGA1UECBMC
V0ExDDAKBgNVBAcTA1NFQTEQMA4GA1UECxMHZHZuaGxzbTEUMBIGA1UEChMLY29t
LnJlc3RhcGkxEjAQBgNVBAMTCXByYXlhZ3VwZDCCAbgwggEsBgcqhkjOOAQBMIIB
HwKBgQD9f1OBHXUSKVLfSpwu7OTn9hG3UjzvRADDHj+AtlEmaUVdQCJR+1k9jVj6
v8X1ujD2y5tVbNeBO4AdNG/yZmC3a5lQpaSfn+gEexAiwk+7qdf+t8Yb+DtX58ao
phUPBPuD9tPFHsMCNVQTWhaRMvZ1864rYdcq7/IiAxmd0UgBxwIVAJdgUI8VIwvM
spK5gqLrhAvwWBz1AoGBAPfhoIXWmz3ey7yrXDa4V7l5lK+7+jrqgvlXTAs9B4Jn
UVlXjrrUWU/mcQcQgYC0SRZxI+hMKBYTt88JMozIpuE8FnqLVHyNKOCjrh4rs6Z1
kW6jfwv6ITVi8ftiegEkO8yk8b6oUZCJqIPf4VrlnwaSi2ZegHtVJWQBTDv+z0kq
A4GFAAKBgQDDSPAaVEUvZBMCow4nzjVcbCxYthIoiSRpynslKGw+rXWm/BtqGK+w
YeyClED51rnYRjUlcRmyat3CykxVSBTS7hwXlrIMmSBO9oVZngE80K7W6zznGMt+
wStefxVhw7pzXfIppzuGn2rKQ3ODLqmuA3zZhFZmAdvVl+pT+c1VwaMhMB8wHQYD
VR0OBBYEFGBdxYTFVqSziq+Kc8nVRhrnqo/4MAsGByqGSM44BAMFAAMvADAsAhR8
SftkKXxIFvZYmBJxVZP5+3RIAQIUJhDcpqLgFX2HPpa3WIcbg5ahRuU=
-----END CERTIFICATE-----
```


[X.509 standard](https://en.wikipedia.org/wiki/X.509)

```
In cryptography, X.509 is a standard that defines the format of public key certificates.

The structure foreseen by the standards is expressed in a formal language, 
Abstract Syntax Notation One (ASN.1).
```

[Abstract Syntax Notation.1/ ASNotation.1](https://en.wikipedia.org/wiki/Abstract_Syntax_Notation_One)

```
an interface description language for defining data structures that can be serialized and deserialized 
in a standard, cross-platform way. It's broadly used in telecommunications and computer networking, 
and especially in cryptography.
```

[Digital Signature Algorithm](https://en.wikipedia.org/wiki/Digital_Signature_Algorithm)

```
a Federal Information Processing Standard for digital signatures.
```

import PEMail into JKS
----------------------

```bash
openssl x509 -outform der -in conf/restapi.pem -out conf/restapi.der
keytool -import -alias restapi -keystore conf/restapi.jks -file conf/restapi.der
```

desc

```bash
$ keytool -import -alias restapi -keystore conf/restapi.jks -file conf/restapi.der
Enter keystore password:  restapi-pass
Re-enter new password: restapi-pass
Owner: CN=prayagupd, O=com.restapi, OU=dvnhlsm, L=SEA, ST=WA, C=US
Issuer: CN=prayagupd, O=com.restapi, OU=dvnhlsm, L=SEA, ST=WA, C=US
Serial number: 5de40d97
Valid from: Sat Jun 17 05:13:46 PDT 2017 until: Fri Sep 15 05:13:46 PDT 2017
Certificate fingerprints:
	 MD5:  11:A5:89:FF:79:31:0D:5B:52:F0:EB:C5:ED:2B:0D:70
	 SHA1: BA:8C:3F:16:20:FF:C8:BF:EC:C2:7A:8E:DA:77:4C:2C:8E:56:F4:60
	 SHA256: A3:C0:64:F9:9B:18:3C:73:37:2F:AF:D8:F7:F3:86:53:79:18:A0:ED:9A:9A:95:61:4C:3B:D0:E4:15:53:38:E1
	 Signature algorithm name: SHA1withDSA
	 Version: 3

Extensions: 

#1: ObjectId: 2.5.29.14 Criticality=false
SubjectKeyIdentifier [
KeyIdentifier [
0000: 60 5D C5 84 C5 56 A4 B3   8A AF 8A 73 C9 D5 46 1A  `]...V.....s..F.
0010: E7 AA 8F F8                                        ....
]
]

Trust this certificate? [no]:  yes
Certificate was added to keystore
```


```bash
keytool -import -alias ca -file /usr/local/jdk1.8/jre/lib/security/nihilism_ca_denver.cer -keystore nihilism_truststore -storepass storepa$$
```


TODO
----

hanshake using `restapi-client.p12:client` at conf3

error

```bash
javax.net.ssl.SSLHandshakeException: sun.security.validator.ValidatorException: No trusted certificate found
	at sun.security.ssl.Alerts.getSSLException(Alerts.java:192)
	at sun.security.ssl.SSLSocketImpl.fatal(SSLSocketImpl.java:1949)
	at sun.security.ssl.Handshaker.fatalSE(Handshaker.java:302)
	at sun.security.ssl.Handshaker.fatalSE(Handshaker.java:296)
	at sun.security.ssl.ClientHandshaker.serverCertificate(ClientHandshaker.java:1509)
	at sun.security.ssl.ClientHandshaker.processMessage(ClientHandshaker.java:216)
	at sun.security.ssl.Handshaker.processLoop(Handshaker.java:979)
	at sun.security.ssl.Handshaker.process_record(Handshaker.java:914)
	at sun.security.ssl.SSLSocketImpl.readRecord(SSLSocketImpl.java:1062)
	at sun.security.ssl.SSLSocketImpl.performInitialHandshake(SSLSocketImpl.java:1375)
	at sun.security.ssl.SSLSocketImpl.startHandshake(SSLSocketImpl.java:1403)
	at sun.security.ssl.SSLSocketImpl.startHandshake(SSLSocketImpl.java:1387)
	at client.api.ClientConnectionThread.run(HttTlsClient.kt:44)
Caused by: sun.security.validator.ValidatorException: No trusted certificate found
	at sun.security.validator.SimpleValidator.buildTrustedChain(SimpleValidator.java:394)
	at sun.security.validator.SimpleValidator.engineValidate(SimpleValidator.java:133)
	at sun.security.validator.Validator.validate(Validator.java:260)
	at sun.security.ssl.X509TrustManagerImpl.validate(X509TrustManagerImpl.java:324)
	at sun.security.ssl.X509TrustManagerImpl.checkTrusted(X509TrustManagerImpl.java:229)
	at sun.security.ssl.X509TrustManagerImpl.checkServerTrusted(X509TrustManagerImpl.java:124)
	at sun.security.ssl.ClientHandshaker.serverCertificate(ClientHandshaker.java:1491)
	... 8 more

```


probably root cert (public key) needs to be installed in machine, but not working in mac

It does show up as localhost in `Keychain Access` UI.

```
sudo security add-trusted-cert -d -r trustRoot -k /Library/Keychains/System.keychain root-ca.pem


```

Run client connection
---------------------

```bash
$ /usr/local/gradle-4.0/bin/gradle run
Starting a Gradle Daemon, 1 busy Daemon could not be reused, use --status for details

> Task :run
[INFO] HttpTlsClient TLSv1 client started
[INFO] ClientConnectionThread SSLSession :
        Protocol : TLSv1
        Cipher suite : TLS_DHE_DSS_WITH_AES_128_CBC_SHA
[INFO] ClientConnectionThread received : HTTP/1.1 200


BUILD SUCCESSFUL in 4s
4 actionable tasks: 3 executed, 1 up-to-date
```
