private SSLSocketFactory newSslSocketFactory()
{
    try
    {
        KeyStore trusted = KeyStore.getInstance ("PKCS12");

        // Get the raw resource, which contains the keystore with
        // your trusted certificates (root and any intermediate certs)
        InputStream in = mCtx.getApplicationContext().getAssets ().open ("cert_keystore.pkcs12");
        try {
            // Initialize the keystore with the provided trusted certificates
            // Provide the password of the keystore
            trusted.load (in, "password".toCharArray ());
        } finally {
            in.close();
        }

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(trusted);


        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify (String hostname, SSLSession session) {

                return hostname.equals ("192.168.1.10"); //The Hostname of your server

            }
        };


        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);


        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        SSLSocketFactory sf = context.getSocketFactory();
        HttpsURLConnection.setDefaultSSLSocketFactory (sf);

        return sf;
    }
    catch (Exception e)
    {
        throw new AssertionError(e);
    }
}
