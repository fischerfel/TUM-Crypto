class ApacheHttpClient {

    /***
     * This is a https get request that bypasses certificate checking and hostname verifier.
     * It uses basis authentication method.
     * It is tested with Apache httpclient-4.4.
     * It dumps the contents of a https page on the console output.
     * It is very similar to http get request, but with the additional customization of
     *   - credential provider, and
     *   - SSLConnectionSocketFactory to bypass certification checking and hostname verifier.
     * @param path String
     * @param username String
     * @param password String
     * @throws IOException
     */
    public void get(String path, String username, String password) throws IOException {
        final CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCredentialsProvider(createCredsProvider(username, password))
                .setSSLSocketFactory(createGenerousSSLSocketFactory())
                .build();

        final CloseableHttpResponse response = httpClient.execute(new HttpGet(path));
        try {
            HttpEntity entity = response.getEntity();
            if (entity == null)
                return;
            System.out.println(EntityUtils.toString(entity));
        } finally {
            response.close();
            httpClient.close();
        }
    }

    private CredentialsProvider createCredsProvider(String username, String password) {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));
        return credsProvider;
    }

    /***
     * 
     * @return SSLConnectionSocketFactory that bypass certificate check and bypass HostnameVerifier
     */
    private SSLConnectionSocketFactory createGenerousSSLSocketFactory() {
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{createGenerousTrustManager()}, new SecureRandom());
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
    }

    private X509TrustManager createGenerousTrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] cert, String s) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] cert, String s) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
    }
}
