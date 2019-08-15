var sc = javax.net.ssl.SSLContext.getInstance("SSL");
    sc.init(null, oo, new java.security.SecureRandom());
    javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
