echo "====================================================="
echo "[INFO] creating root cert"
echo "====================================================="
# -nodes: no passphrase on the CA key (dev environment)
openssl genrsa -out root-ca.key 4096
openssl req -new -sha256 -key root-ca.key -out root-ca.csr -config root-ca.conf
# -extensions v3_ca embeds basicConstraints=CA:TRUE so it is recognised as a CA by OpenSSL/Java
openssl x509 -req -sha256 -days 3650 -in root-ca.csr -signkey root-ca.key \
    -extensions v3_ca -extfile root-ca.conf \
    -out root-ca.pem
