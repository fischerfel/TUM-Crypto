    public static DefaultHttpClient getHttpClient (Context context, String serverPort) throws   NoSuchAlgorithmException, 
                                                                                            CertificateException, 
                                                                                            IOException, 
                                                                                            KeyStoreException, 
                                                                                            KeyManagementException, 
                                                                                            UnrecoverableKeyException {
    KeyStore keyStoreBouncyCastle = KeyStore.getInstance("BKS");
    InputStream in = context.getResources().openRawResource(R.raw.elaboro_keystore_bks);
    try {
        keyStoreBouncyCastle.load(in, "orobale".toCharArray());
    } finally {
        in.close();
    }
    SSLSocketFactory sslSocketFactory=new SSLSocketFactory(keyStoreBouncyCastle);
    sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

    SchemeRegistry registry = new SchemeRegistry();
    registry.register(new Scheme("https", sslSocketFactory, Integer.parseInt(serverPort)));

    final HttpParams httpParams = new BasicHttpParams();
    HttpConnectionParams.setConnectionTimeout(httpParams, Constants.CONNECTION_TIMEOUT_IN_MILLIS);
    HttpConnectionParams.setSoTimeout(        httpParams, Constants.CONNECTION_TIMEOUT_IN_MILLIS);

    DefaultHttpClient defaultHttpClient=new DefaultHttpClient(new SingleClientConnManager(httpParams, registry), httpParams);
    defaultHttpClient.setRoutePlanner(new DefaultHttpRoutePlanner(registry));
    return defaultHttpClient;
} 
