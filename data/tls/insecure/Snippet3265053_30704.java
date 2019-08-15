SSLContext sc = SSLContext.getInstance("TLS");
sc.init(null, new TrustManager [] { new MyTrustManager() }, new SecureRandom());
