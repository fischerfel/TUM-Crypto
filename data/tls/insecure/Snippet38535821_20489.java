SSLContext sslContext = SSLContext.getInstance("SSL");
sslContext.init(null, new TrustManager[]{new MyTrustManager()}, null);
HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
