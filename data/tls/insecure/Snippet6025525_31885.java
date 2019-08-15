SSLContext sc = SSLContext.getInstance("SSL");
sc.init(null, new TrustManager[] { trm }, null);
HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
