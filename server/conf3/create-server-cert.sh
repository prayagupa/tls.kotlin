openssl genrsa -out restapi-server.key 2048
openssl req -new -sha256  -key restapi-server.key -out restapi-server.csr -config restapi-server.conf

openssl x509 -req -sha256 -days 13210 -in restapi-server.csr -signkey restapi-server.key -CA root-ca.pem -CAkey root-ca.key -CAcreateserial -out restapi-server.cert

echo ""
echo "[INFO] creating pkcs#12 for server cert"
echo ""

openssl pkcs12 -export -name restapi-server -in restapi-server.cert -out restapi-server.p12 -inkey restapi-server.key -CAfile root-ca.pem -caname restapi-root -chain 
