#!/usr/bin/env bash
pwd
openssl req -new -x509 -days 9999 -keyout src/test/resources/certs/ca-key.pem -out src/test/resources/certs/ca-crt.pem \
-subj "/C=US/ST=Georgia/L=Marietta/O=SmallBizDevOps/OU=BAMF/CN=smallbizdevops.com"
openssl genrsa -out src/test/resources/certs/server-key.pem 4096
openssl req -new -key src/test/resources/certs/server-key.pem -out src/test/resources/certs/server-csr.pem \
-subj "/C=US/ST=Georgia/L=Marietta/O=SmallBizDevOps/OU=BAMF/CN=smallbizdevops.com"
openssl x509 -req -days 9999 -in src/test/resources/certs/server-csr.pem -CA src/test/resources/certs/ca-crt.pem \
-CAkey src/test/resources/certs/ca-key.pem -CAcreateserial -out src/test/resources/certs/server-crt.pem
openssl verify -CAfile src/test/resources/certs/ca-crt.pem src/test/resources/certs/server-crt.pem
openssl genrsa -out src/test/resources/certs/client1-key.pem 4096
openssl req -new -key src/test/resources/certs/client1-key.pem -out src/test/resources/certs/client1-csr.pem \
-subj "/C=US/ST=Georgia/L=Marietta/O=SmallBizDevOps/OU=BAMF/CN=smallbizdevops.com"
openssl x509 -req -days 9999 -in src/test/resources/certs/client1-csr.pem -CA src/test/resources/certs/ca-crt.pem \
-CAkey src/test/resources/certs/ca-key.pem -CAcreateserial -out src/test/resources/certs/client1-crt.pem
openssl verify -CAfile src/test/resources/certs/ca-crt.pem src/test/resources/certs/client1-crt.pem
openssl pkcs12 -export -out src/test/resources/certs/client1.pfx -inkey src/test/resources/certs/client1-key.pem \
-in src/test/resources/certs/client1-crt.pem