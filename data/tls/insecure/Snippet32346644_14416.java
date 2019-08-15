 SSLContext sc = SSLContext.getInstance("TLSv1");
 // Init the SSLContext with a TrustManager[] and SecureRandom()
 sc.init(null, trustCerts, new java.security.SecureRandom());
