    KeyStore keystore = getKeyStore();//Implemented somewhere else and working ok
    String password = "changeme";

    SSLContext context = SSLContext.getInstance("SSL");
    KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmfactory.init(keystore, password.toCharArray());
    context.init(kmfactory.getKeyManagers(), new TrustManager[] { new X509TrustManager() {
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }
        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    } }, new SecureRandom());
    SSLSocketFactory sf = new SSLSocketFactory(context);

    Scheme httpsScheme = new Scheme("https", 443, sf);
    SchemeRegistry schemeRegistry = new SchemeRegistry();
    schemeRegistry.register(httpsScheme);
    ClientConnectionManager cm = new SingleClientConnManager(schemeRegistry);
    HttpClient client = new DefaultHttpClient(cm);

    HttpGet get = new HttpGet("https://theurl.com");
    HttpResponse response = client.execute(get);
    System.out.println(EntityUtils.toString(response.getEntity(), "UTF-8"));
