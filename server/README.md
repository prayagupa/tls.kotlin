create keystore with public/private keys
------------------------------------------

1) example to create PP KeyStore

```bash
$ mkdir conf

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

```
$ keytool -list -keystore conf/eccountKeyStore.jks 
Enter keystore password:  

Keystore type: JKS
Keystore provider: SUN

Your keystore contains 1 entry

eccount, Nov 14, 2016, PrivateKeyEntry, 
Certificate fingerprint (SHA1): E5:2E:B2:C5:17:DA:08:D4:1B:97:C4:33:E1:9B:3B:08:24:69:31:0D
```

create PublicKeyCS(PKCS12)
--------------------------

so that client of this server can handshake with it.

```
keytool -keystore conf/restapi.jks -genkeypair -alias restapi -dname 'CN=prayagupd,O=com.restapi,OU=dvnhlsm,L=SEA,ST=WA,C=US'       

keytool -keystore conf/restapi.jks -exportcert -alias restapi | openssl x509 -inform der -text

keytool -importkeystore -srckeystore conf/restapi.jks \
       -destkeystore conf/restapi.p12 \
       -srcstoretype jks \
       -deststoretype pkcs12
       
openssl pkcs12 -in conf/restapi.p12 -out conf/restapi.pem
openssl x509 -text -in conf/restapi.pem
openssl dsa -text -in conf/restapi.pem
```

desc
----

```
keytool -keystore conf/restapi.jks -genkeypair -alias restapi -dname 'CN=prayagupd,O=com.restapi,OU=dvnhlsm,L=SEA,ST=WA,C=US'
Enter keystore password:  restapi-password
Re-enter new password: restapi-password
Enter key password for <restapi>
	(RETURN if same as keystore password): 
	
keytool -keystore conf/restapi.jks -exportcert -alias restapi | openssl x509 -inform der -text

keytool -importkeystore -srckeystore conf/eccountKeyStore.jks \
       -destkeystore eccount-cert.p12 \
       -srcstoretype jks \
       -deststoretype pkcs12
```

```
keytool -keystore conf/restapi.jks -exportcert -alias restapi | openssl x509 -inform der -text
 
Enter keystore password:  restapi-password
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

```
$ keytool -importkeystore -srckeystore conf/restapi.jks \
→        -destkeystore conf/restapi.p12 \
→        -srcstoretype jks \
→        -deststoretype pkcs12
Enter destination keystore password:  
Re-enter new password: 
Enter source keystore password:  
Entry for alias restapi successfully imported.
Import command completed:  1 entries successfully imported, 0 entries failed or cancelled
```

```
$ ll conf/
total 24
18952083 -rw-r--r--  1 as18  NORD\Domain Users  1280 Jun 17 05:13 restapi.jks
18952427 -rw-r--r--  1 as18  NORD\Domain Users  1614 Jun 17 05:20 restapi.p12
```


```
$ openssl pkcs12 -in conf/restapi.p12 -out conf/restapi.pem
Enter Import Password:
MAC verified OK
Enter PEM pass phrase:
Verifying - Enter PEM pass phrase:
```

```
ll conf/
18952083 -rw-r--r--  1 as18  NORD\Domain Users  1280 Jun 17 05:13 restapi.jks
18952427 -rw-r--r--  1 as18  NORD\Domain Users  1614 Jun 17 05:20 restapi.p12
18952651 -rw-r--r--  1 as18  NORD\Domain Users  2278 Jun 17 05:25 restapi.pem
```

```
$ openssl x509 -text -in conf/restapi.pem
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

```
$ openssl dsa -text -in conf/restapi.pem
read DSA key
Enter pass phrase for conf/restapi.pem:
Private-Key: (1024 bit)
priv:
    70:0f:11:d2:50:2e:03:11:e9:91:1a:27:8b:20:2a:
    87:fc:48:b7:f6
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
writing DSA key
-----BEGIN DSA PRIVATE KEY-----
MIIBvAIBAAKBgQD9f1OBHXUSKVLfSpwu7OTn9hG3UjzvRADDHj+AtlEmaUVdQCJR
+1k9jVj6v8X1ujD2y5tVbNeBO4AdNG/yZmC3a5lQpaSfn+gEexAiwk+7qdf+t8Yb
+DtX58aophUPBPuD9tPFHsMCNVQTWhaRMvZ1864rYdcq7/IiAxmd0UgBxwIVAJdg
UI8VIwvMspK5gqLrhAvwWBz1AoGBAPfhoIXWmz3ey7yrXDa4V7l5lK+7+jrqgvlX
TAs9B4JnUVlXjrrUWU/mcQcQgYC0SRZxI+hMKBYTt88JMozIpuE8FnqLVHyNKOCj
rh4rs6Z1kW6jfwv6ITVi8ftiegEkO8yk8b6oUZCJqIPf4VrlnwaSi2ZegHtVJWQB
TDv+z0kqAoGBAMNI8BpURS9kEwKjDifONVxsLFi2EiiJJGnKeyUobD6tdab8G2oY
r7Bh7IKUQPnWudhGNSVxGbJq3cLKTFVIFNLuHBeWsgyZIE72hVmeATzQrtbrPOcY
y37BK15/FWHDunNd8imnO4afaspDc4Muqa4DfNmEVmYB29WX6lP5zVXBAhRwDxHS
UC4DEemRGieLICqH/Ei39g==
-----END DSA PRIVATE KEY-----
```


```
$ keytool -list -keystore conf/restapi.jks
Enter keystore password:  

Keystore type: JKS
Keystore provider: SUN

Your keystore contains 1 entry

restapi, Jun 17, 2017, PrivateKeyEntry, 
Certificate fingerprint (SHA1): BA:8C:3F:16:20:FF:C8:BF:EC:C2:7A:8E:DA:77:4C:2C:8E:56:F4:60
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

$ curl -v -XGET --cert conf/restapi.jks:restapi-password https://localhost:9999
* Rebuilt URL to: https://localhost:9999/
*   Trying ::1...
* Connected to localhost (::1) port 9999 (#0)
* WARNING: SSL: Certificate type not set, assuming PKCS#12 format.
* SSL: Couldn't make sense of the data in the certificate "conf/restapi.jks" and its private key.
* Closing connection 0
curl: (58) SSL: Couldn't make sense of the data in the certificate "conf/restapi.jks" and its private key.


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
