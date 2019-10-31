
openssl genrsa -out forsrc.pem 2048 

openssl req -new -key forsrc.pem -out forsrc.csr

openssl x509 -req -in forsrc.csr -extensions v3_ca -signkey forsrc.pem -out forsrc.crt