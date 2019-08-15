KeyStore truststore = KeyStore.getInstance("JKS");
truststore.load(myKeystoreInputStream, myKeystorePassword.toCharArray());

TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
tmf.init(truststore);

SSLContext sslContext = SSLContext.getInstance("SSL");
sslContext.init(null, tmf.getTrustManagers(), null);
HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
