SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, dummyTrustManager, new java.security.SecureRandom());
