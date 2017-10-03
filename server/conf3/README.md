
create root CA
--------------

```
./create-root-cert.sh
```

create server-cert
------------------

final file - server.pkcs#12 with password

```
./create-server-cert.sh
```

create client-cert
------------------

create a create CSR(PKCS#10 format) - http://fileformats.archiveteam.org/wiki/PKCS10

final file - client.pkcs#12 with password

```
./create-client-cert.sh
```
