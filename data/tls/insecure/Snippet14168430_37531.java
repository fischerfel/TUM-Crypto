KeyStore keystore = KeyStore.getInstance("JKS");
keystore.load(new FileInputStream("KeyStore"), "password".toCharArray());
TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
tmf.init(keystore);

SSLContext context = SSLContext.getInstance("TLS");
TrustManager[] trustManagers = tmf.getTrustManagers();

context.init(null, trustManagers, null);

SSLSocketFactory sf = context.getSocketFactory();
return (SSLSocket) sf.createSocket(host, port);
