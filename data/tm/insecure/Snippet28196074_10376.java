@Test
    public void withTrustManeger() throws Exception {
        DefaultHttpClient httpclient = buildhttpClient();
        HttpGet httpGet = new HttpGet("https://urlto.esb.com");
        HttpResponse response = httpclient.execute( httpGet );

        HttpEntity httpEntity = response.getEntity();
        InputStream inputStream = httpEntity.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        inputStream.close();
        String jsonText = sb.toString();
        System.out.println(jsonText);
    }

    private DefaultHttpClient buildhttpClient() throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, getTrustingManager(), new java.security.SecureRandom());

        SSLSocketFactory socketFactory = new SSLSocketFactory(sc);
        Scheme sch = new Scheme("https", 443, socketFactory);
        httpclient.getConnectionManager().getSchemeRegistry().register(sch);
        return httpclient;
    }

    private TrustManager[] getTrustingManager() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                // Do nothing               
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                // Do nothing
            }

        } };
        return trustAllCerts;
    }
