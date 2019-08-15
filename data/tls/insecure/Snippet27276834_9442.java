    KeyStore ks = KeyStore.getInstance("Windows-MY");
    ks.load(null, null);

    KeyStore ts = KeyStore.getInstance("Windows-ROOT");
    ts.load(null, null);

    TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(ts);

    KeyManagerFactory kmf = KeyManagerFactory
                .getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(ks, new char[0]);

    SSLContext ctx = SSLContext.getInstance("TLS");
    ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);    

    URL url = new URL("https://some.web.site.org");
    javax.net.ssl.HttpsURLConnection urlConnection = 
            (javax.net.ssl.HttpsURLConnection) url.openConnection();
    urlConnection.setSSLSocketFactory(ctx.getSocketFactory());
    urlConnection.connect();
    try (InputStream in = urlConnection.getInputStream();) {
        byte[] chunk = new byte[1024];
        for (int len; (len = in.read(chunk)) > -1;) {
            System.out.write(chunk, 0, len);
        }
    } finally {
        urlConnection.disconnect();
    }
