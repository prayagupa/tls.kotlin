openssl genrsa -out restapi-server.key 2048
openssl req -new -sha256 -key restapi-server.key -out restapi-server.csr -config restapi-server.conf

# Sign with the Root CA (not -signkey which is self-sign only).
# -extensions req_ext -extfile embeds the SAN into the issued cert (required by Java 11+ hostname verification).
openssl x509 -req -sha256 -days 13210 -in restapi-server.csr \
    -CA root-ca.pem -CAkey root-ca.key -CAcreateserial \
    -extensions req_ext -extfile restapi-server.conf \
    -out restapi-server.cert

echo ""
echo "[INFO] creating pkcs#12 for server cert"
echo ""

openssl pkcs12 -export -name restapi-server \
    -in restapi-server.cert -inkey restapi-server.key \
    -CAfile root-ca.pem -caname restapi-root -chain \
    -out restapi-server.p12 \
    -passout pass:server
