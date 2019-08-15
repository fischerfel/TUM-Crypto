TrustManager[] trustAllCerts = new TrustManager[] { ... };

// Install the all-trusting trust manager
try {
    SSLContext sc = SSLContext.getInstance("TLS");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
