
```
openssl req -newkey rsa:1024 -sha1 -keyout rootkey.pem -out rootreq.pem -config root-ca.conf 
Generating a 1024 bit RSA private key
..............................................................................++++++
...................................++++++
writing new private key to 'rootkey.pem'
Enter PEM pass phrase:
Verifying - Enter PEM pass phrase:
-----

openssl x509 -req -in rootreq.pem -sha1 -signkey rootkey.pem -out rootcert.pem
Signature ok
subject=/C=US/ST=Seattle/L=Seattle/O=Duwamish/OU=SC/emailAddress=prayag.upd@gmail.com/CN=localhost
Getting Private key
Enter pass phrase for rootkey.pem:


openssl verify rootcert.pem 
rootcert.pem: /C=US/ST=Seattle/L=Seattle/O=Duwamish/OU=SC/emailAddress=prayag.upd@gmail.com/CN=localhost
error 18 at 0 depth lookup:self signed certificate
OK
```
