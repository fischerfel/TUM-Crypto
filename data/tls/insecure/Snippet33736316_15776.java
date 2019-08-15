public void login()
    {
    try {
        KeyStore ks = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
        ks.load(null, null);
        String kalg = KeyManagerFactory.getDefaultAlgorithm();
        System.out.println(kalg);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(kalg);
        kmf.init(ks, null);
        String talg = TrustManagerFactory.getDefaultAlgorithm();
        System.out.println(talg);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(talg);
        KeyStore ts;
        ts = KeyStore.getInstance("Windows-ROOT", "SunMSCAPI");

        ts.load(null, null);
        tmf.init(ts);
        TrustManager tm[] = tmf.getTrustManagers();
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(kmf.getKeyManagers(), tm, new java.security.SecureRandom());
        HttpsURLConnection
            .setDefaultSSLSocketFactory(sc.getSocketFactory());
        URL url = new URL("https://xxxxxx/");
        HttpsURLConnection httpsCon = (HttpsURLConnection) url
            .openConnection();
        InputStream is = httpsCon.getInputStream();
        httpsCon.getHeaderFields();
        String str =httpsCon.getHeaderField("Set-Cookie");
        System.out.println(httpsCon.getResponseMessage());
        int c;
        StringBuffer sb = new StringBuffer();
        while ((c = is.read()) >= 0) {
        System.out.print((char)c);
        sb.append((char) c);
        }
        is.close();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    }
