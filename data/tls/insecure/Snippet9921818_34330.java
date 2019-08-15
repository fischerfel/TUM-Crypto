SSLContext sc = SSLContext.getInstance("SSL");
sc.init(myKeyManagerFactory.getKeyManagers(), myTrustManagerArray, new java.security.SecureRandom());
HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
