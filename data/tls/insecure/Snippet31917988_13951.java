public ApiService() {
    mClient = new OkHttpClient();
    mClient.setConnectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS);
    mClient.setReadTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS);
    mClient.setCache(getCache());
    mClient.setCertificatePinner(getPinnedCerts());
    mClient.setSslSocketFactory(getSSL());
}

protected SSLSocketFactory getSSL() {
    try {
        KeyStore trusted = KeyStore.getInstance("BKS");
        InputStream in = Beadict.getAppContext().getResources().openRawResource(R.raw.mytruststore);
        trusted.load(in, "pwd".toCharArray());
        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trusted);
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
        return sslContext.getSocketFactory();
    } catch(Exception e) {
        e.printStackTrace();
    }
    return null;
}

public CertificatePinner getPinnedCerts() {
    return new CertificatePinner.Builder()
            .add("domain.com", "sha1/theSha=")
            .build();
}
