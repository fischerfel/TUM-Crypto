SSLContext sCtxt = SSLContext.getInstance("SSL"); 
sCtxt.init(null, new TrustManager[]{new EasyTrustManager()}, new java.security.SecureRandom()); 
