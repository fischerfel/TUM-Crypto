TrustManagerFactory tmf = TrustManagerFactory
        .getInstance(TrustManagerFactory.getDefaultAlgorithm());
// Using null here initialises the TMF with the default trust store.
tmf.init((KeyStore) null);

// Get hold of the default trust manager
X509TrustManager x509Tm = null;
for (TrustManager tm : tmf.getTrustManagers()) {
    if (tm instanceof X509TrustManager) {
        x509Tm = (X509TrustManager) tm;
        break;
    }
}

// Wrap it in your own class.
final X509TrustManager finalTm = x509Tm;
X509TrustManager customTm = new X509TrustManager() {
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return finalTm.getAcceptedIssuers();
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain,
            String authType) throws CertificateException {
        finalTm.checkServerTrusted(chain, authType);
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain,
            String authType) throws CertificateException {
        finalTm.checkClientTrusted(chain, authType);
    }
};

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, new TrustManager[] { customTm }, null);

// You don't have to set this as the default context,
// it depends on the library you're using.
SSLContext.setDefault(sslContext);
