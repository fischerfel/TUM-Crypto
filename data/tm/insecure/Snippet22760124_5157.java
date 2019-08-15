private InputStream Get(URL url,String keyStoreString, String keyStorePassword) {
    SSLSocketFactory sslFactory;
    try {
        sslFactory = getSSLSocketFactory(keyStoreString, keyStorePassword);     
        javax.net.ssl.HttpsURLConnection
                .setDefaultHostnameVerifier(new  javax.net.ssl.HostnameVerifier() {
                    public boolean verify(String hostname,
                            javax.net.ssl.SSLSession sslSession) {
                        if (hostname.equals("vmm")) {
                            return true;
                        }
                        return false;
                    }
                });
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setSSLSocketFactory(sslFactory);
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");
        InputStream responseStream = con.getInputStream();
        return responseStream;
      }catch (Exception e) {
        //log errer
      }
}
private SSLSocketFactory getSSLSocketFactory(String keyStoreString,
        String password) throws KeyStoreException,
        NoSuchAlgorithmException, CertificateException, IOException,
        UnrecoverableKeyException, KeyManagementException, AzureException {
      KeyStore ks = getKeyStore(keyStoreString, password);
      KeyManagerFactory keyManagerFactory = KeyManagerFactory
            .getInstance("SunX509");
      keyManagerFactory.init(ks, password.toCharArray());
      // Trustmanager which trusts all certificate. Not a good idea in
      // production code.
      final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        @Override
        public void checkClientTrusted(final X509Certificate[] chain,
                final String authType) {
        }

        @Override
        public void checkServerTrusted(final X509Certificate[] chain,
                final String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    } };

    SSLContext context = SSLContext.getInstance("TLS");
    context.init(keyManagerFactory.getKeyManagers(), trustAllCerts,
            new SecureRandom());

    return context.getSocketFactory();
}
