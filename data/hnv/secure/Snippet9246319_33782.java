public HttpClient getNewHttpClient() {
    try {
        InputStream in = null;
        // Load default system keystore
        KeyStore trusted = KeyStore.getInstance(KeyStore.getDefaultType()); 
        try {
            in = new BufferedInputStream(new FileInputStream(System.getProperty("javax.net.ssl.trustStore"))); // Normally: "/system/etc/security/cacerts.bks"
            trusted.load(in, null); // no password is "changeit"
        } finally {
            if (in != null) {
                in.close();
                in = null;
            }
        }

        // Load application keystore & merge with system
        try {
            KeyStore appTrusted = KeyStore.getInstance("BKS"); 
            in = context.getResources().openRawResource(R.raw.mykeystore);
            appTrusted.load(in, null); // no password is "changeit"
            for (Enumeration<String> e = appTrusted.aliases(); e.hasMoreElements();) {
                final String alias = e.nextElement();
                final KeyStore.Entry entry = appTrusted.getEntry(alias, null);
                trusted.setEntry(System.currentTimeMillis() + ":" + alias, entry, null);
            }
        } finally {
            if (in != null) {
                in.close();
                in = null;
            }
        }

        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

        SSLSocketFactory sf = new SSLSocketFactory(trusted);
        sf.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", sf, 443));

        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

        return new DefaultHttpClient(ccm, params);
    } catch (Exception e) {
        return new DefaultHttpClient();
    }
}
