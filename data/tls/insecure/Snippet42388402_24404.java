env.put("java.naming.ldap.factory.socket", CustomTLSSSLSocketFactory.class.getName);

CustomTLSSSLSocketFactory extends SSSLSocketFactory {
public CustomTLSSSLSocketFactory() {
TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        factory.init((KeyStore) null);
        TrustManager[] defaultTrustManagers = factory.getTrustManagers();

        // create the real socket factory
        SSLContext sc = SSLContext.getInstance("TLS"); //$NON-NLS-1$
        sc.init(null, defaultTrustManagers, null);
        delegate = sc.getSocketFactory();
    }
}
