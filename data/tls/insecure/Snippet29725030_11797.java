        SSLContext context = SSLContext.getInstance("TLS");
        MyTrustManager tm = new MyTrustManager();
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        context.init(null, new TrustManager[] { tm }, sr);
        SSLContext.setDefault(context);
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
