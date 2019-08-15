HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
SSLContext context = SSLContext.getInstance("TLS");
context.init(null, new X509TrustManager[]{new NullX509TrustManager()}, new SecureRandom());
HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
