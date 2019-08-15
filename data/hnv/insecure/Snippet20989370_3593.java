   public static DefaultHttpClient getNewHttpClient() {
    try {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);
        SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", sf, 443));

        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

        return new DefaultHttpClient(ccm, params);

    } catch (UnrecoverableKeyException e) {
        return new DefaultHttpClient();
    } catch (NoSuchAlgorithmException e) {
        return new DefaultHttpClient();
    } catch (CertificateException e) {
        return new DefaultHttpClient();
    } catch (IOException e) {
        return new DefaultHttpClient();
    } catch (KeyStoreException e) {
        return new DefaultHttpClient();
    } catch (KeyManagementException e) {
        return new DefaultHttpClient();
    }
}
