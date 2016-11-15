TLS server/ client communication using Public/Private Key
-------------------------------

client loaded with Server Certificates            |          Server
--------------------------------------------------|------------------------------------
Message --> [Public Key]-->Encrypted Message-->   |      [Private Key]-->Message

The trick in a key pair is to keep one key secret (the private key) and to distribute the 
other key (the public key) to everybody. 
Anybody can send you an encrypted message, that only you will be able to decrypt.

http://tldp.org/HOWTO/SSL-Certificates-HOWTO/x64.html

https://docs.oracle.com/cd/E19509-01/820-3503/ggbgc/index.html

![](https://docs.oracle.com/cd/E19509-01/820-3503/images/encryption-and-decryption.gif)


How do you know that you are dealing with the right person or rather the right web site. 
------------------------------------------------------------------------------------------

This right person/web server, you have to implicitly trust: 
you have his/her certificate loaded in your browser/application (a root Certificate). 


[Trust Store vs Key Store - creating with keytool](http://stackoverflow.com/a/6341566/432903)

```
Essentially, the keystore in javax.net.ssl.keyStore is meant to contain your private keys and certificates, 

whereas the javax.net.ssl.trustStore is meant to contain the CA certificates you're willing to trust when a remote party 
presents its certificate.
```
create keystore
-----------------

```bash
prayagupd at prayagupd-vbox in /media/sf_programming/https-server-kotlin/src
$ mkdir conf

prayagupd at prayagupd-vbox in /media/sf_programming/https-server-kotlin/src
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


refs
-----

https://en.wikipedia.org/wiki/Transport_Layer_Security#Certificate_authorities


