echo "====================================================="
echo "[INFO] creating root cert"
echo "====================================================="
openssl req -newkey rsa:1024 -sha1 -keyout root-ca.key -out root-ca.csr -config root-ca.conf
openssl x509 -req -in root-ca.csr -sha1 -signkey root-ca.key -out root-ca.pem
