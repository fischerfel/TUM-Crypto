SSLContext context = SSLContext.getInstance("TLS");
context.init(new KeyManager[] { new ClientKeyManager() }, 
    new TrustManager[] { new ClientTrustManager() }, 
    new SecureRandom());
HttpsURLConnection.setDefaultSSLSocketFactory(new ManagedSSLSocketFactory(context.getSocketFactory()));
