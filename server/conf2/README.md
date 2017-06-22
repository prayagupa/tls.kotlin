
**1- create private key**

```bash
$ openssl genrsa -out restapi-private.PEMail 4096
Generating RSA private key, 4096 bit long modulus
.................................................................................................................................++
..................++
e is 65537 (0x10001)
```

**2- create Certificate Signing Request(CSR) from private key**

Once the private key is generated a `Certificate Signing Request` can be generated. 
The CertificateSR is then used in one of two ways. 

Ideally, the CSR will be sent to a Certificate Authority(CAs), 
such as Thawte or Verisign who will verify the identity of the requestor and issue a signed certificate. 

```bash
$ openssl req -new -key restapi-private.PEMail -out restapi-private.csr
You are about to be asked to enter information that will be incorporated
into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields but you can leave some blank
For some fields there will be a default value,
If you enter '.', the field will be left blank.
-----
Country Name (2 letter code) [AU]:US
State or Province Name (full name) [Some-State]:WA
Locality Name (eg, city) []:SEA
Organization Name (eg, company) [Internet Widgits Pty Ltd]:prayagupd               
Organizational Unit Name (eg, section) []:engineering
Common Name (e.g. server FQDN or YOUR name) []:prayagupd
Email Address []:prayag.upd@gmail.com

Please enter the following 'extra' attributes
to be sent with your certificate request
A challenge password []:restapi-private
An optional company name []:prayagupd
```

**3- Generating a Self-Signed Certificate using Certificate SR**

```bash
$ openssl x509 -req -days 365 -in restapi-private.csr -signkey restapi-private.PEMail -out restapi.crt
Signature ok
subject=/C=US/ST=WA/L=SEA/O=prayagupd/OU=engineering/CN=prayagupd/emailAddress=prayag.upd@gmail.com
Getting Private key
```

resources
-----------

https://www.akadia.com/services/ssh_test_certificate.html

https://stackoverflow.com/a/31984753/432903

