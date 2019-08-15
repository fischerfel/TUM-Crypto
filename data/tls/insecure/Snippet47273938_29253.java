String certStr = context.getString(R.string.caApi);
X509Certificate ca = SecurityHelper.readCert(certStr);

TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
ks.load(null);
ks.setCertificateEntry("caCert", ca);

tmf.init(ks);

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, tmf.getTrustManagers(), null);
HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
