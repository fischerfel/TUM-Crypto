    public class SSLContextManager
{
    public static SSLContext getSSLContext(Context context) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, KeyManagementException, NoSuchProviderException
    {
        // Load CAs from an InputStream
        CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
        InputStream cert = context.getResources().openRawResource(R.raw.my_cert); // Place your 'my_cert.crt' file in `res/raw`
        Certificate ca;
        try
        {
            ca = cf.generateCertificate(cert);
        }
        finally
        {
            cert.close();
        }

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        return sslContext;
    }

    public static SSLContext getTrustAllSSLContext() throws NoSuchAlgorithmException, KeyManagementException
    {
        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType){
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        // Install the all-trusting trust manager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        return sslContext;
    }
}
