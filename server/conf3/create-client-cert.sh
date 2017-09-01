openssl genrsa -out restapi-client.key 2048

# Use common name = <tomcat-user.xml's user say 'admin'>, department = Tomcat Client CSR
# take Cert Signing Request from API user, and respond with a restapi-client.cert

openssl req -new -sha256  -key restapi-client.key -out restapi-client.csr -config restapi-client.conf

openssl x509 -req -sha256 -days 13210 -in restapi-client.csr -signkey restapi-client.key -CA root-ca.pem -CAkey root-ca.key -CAcreateserial -out restapi-client.cert

echo ""
echo "[INFO] creating pkcs#12 for client"
echo ""

openssl pkcs12 -export -name restapi-client -in restapi-client.cert -out restapi-client.p12 -inkey restapi-client.key -CAfile root-ca.pem -caname restapi-root -chain
