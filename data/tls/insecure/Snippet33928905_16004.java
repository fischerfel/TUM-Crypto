KeyStore androidKeyStore = KeyStore.getInstance(LocalKeyStore.ANDROID_KEYSTORE);
androidKeyStore.load(null);

X509Certificate signedClientCertificate = (X509Certificate)androidKeyStore.getCertificate("X-Signed");
KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)androidKeyStore.getEntry("X", null);

X509ExtendedKeyManager keyManager = new X509ExtendedKeyManager() {
    @Override
    public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
        return clientCertificateAlias;
    }
    @Override
    public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
        return null; // different if you're validating the server's cert
    }
    @Override
    public X509Certificate[] getCertificateChain(String alias) {
        return new X509Certificate[] { signedClientCertificate };
    }
    @Override
    public String[] getClientAliases(String keyType, Principal[] issuers) {
        return new String[]{ "X" };
    }

    @Override
    public String[] getServerAliases(String keyType, Principal[] issuers) {
        return null; // different if you're validating server's cert
    }
    @Override
    public PrivateKey getPrivateKey(String alias) {
        if(alias != clientCertificateAlias) {
            Log.e(TAG, String.format("X509ExtendedKeyManager is asking for privateKey with unknown alias %s. Expecting it to ask for %s", alias, clientCertificateAlias));
            return null;
        }
        return privateKeyEntry.getPrivateKey();
    }
};

X509TrustManager trustServerCertificates = new X509TrustManager() {
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // do nothing, this method doesn't get called
    }
    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) 
        // code to validate server's cert in here
    }
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null; // any issuer
    }
};

m_sslContext = SSLContext.getInstance("TLS");
m_sslContext.init(new KeyManager[]{ keyManager }, new TrustManager[] { trustServerCertificates }, null);

// later on

conn = (HttpURLConnection)url.openConnection();
SSLContext sslContext = m_sslContext;

if(conn instanceof HttpsURLConnection && sslContext != null) {
    ((HttpsURLConnection)conn).setSSLSocketFactory(sslContext.getSocketFactory());
}
