 StringBuilder sb = new StringBuilder();
    SSLContext sslContext = null;

    // Error Handling
    try {
        sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }}, new SecureRandom());

        // Credential
        NTCredentials credentials = new NTCredentials("username", "password", "Hostname", "domain");
        AuthScope scope = new AuthScope("Hostname", 17963);
        //KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        Scheme sch = new Scheme("https", socketFactory, 17963);

        DefaultHttpClient httpClient = new DefaultHttpClient();

        httpClient.getCredentialsProvider().setCredentials(scope, credentials);
        httpClient.getConnectionManager().getSchemeRegistry().register(sch);


        // HttpHost host = new HttpHost("HostName", 17963, "https");

        HttpGet get = new HttpGet(_spHostApi);

        HttpResponse response = httpClient.execute(get);

        InputStreamReader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
        BufferedReader rd = new BufferedReader(reader);

        String line = "";

        while ((line = rd.readLine()) != null)
            sb.append(line);

        httpClient.getConnectionManager().shutdown();
    }
    catch (Exception ex) { sb.append(ex.toString()); }
