TrustManagerFactory tmf;
KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
keyStore.load(null,null);
keyStore.setCertificateEntry("CA", cert);

tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(keyStore);
X509Certificate[] chain = new X509Certificate[]{cert};

SSLContext context = SSLContext.getInstance("TLS");
context.init(null, tmf.getTrustManagers(), null);

HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
