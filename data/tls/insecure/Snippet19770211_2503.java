SSLContext context = weblogic.security.SSL.SSLContext.getInstance("TLS");
Certificate[] certs = ...; // load the certificates from your prop-files keystore
PrivateKey privKey = ...; // load the private key from your keystore
context.loadLocalIdentity(certs, key);

service.getRequestContext().put(JAXWSProperties.SSL_SOCKET_FACTORY, context.getSocketFactory());
