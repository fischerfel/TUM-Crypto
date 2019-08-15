SSLContext sc = SSLContext.getInstance("TLS");
sc.init(null, new TrustManager[] { new DummyTrustManager() }, new java.security.SecureRandom());
