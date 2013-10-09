javaMPNSNotification
====================

MPNS notification using different http clients from java
Apache HTTP Client works correctly.
JavaxNetSSLClient works corectly.
JettyHTTPClient is unable to connect and gives an EOFException.

Inside directory src/ieSigning
Need to add talk.to.key file.
Then create PKCS12 certificate using
    openssl pkcs12 -inkey talk.to.key -in talk.to.crt -export -out talk.to.p12
    On prompt set password as directi
Next import the above created certificate in a browser and then export the certificate with full certificate chain in file talk.to_ie.pfx
While exporting keep the format as PKCS12.
