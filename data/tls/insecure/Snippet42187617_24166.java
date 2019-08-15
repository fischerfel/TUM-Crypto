private SSLSocketFactory newSslSocketFactory() {
    try {
        // Get an instance of the Bouncy Castle KeyStore format
        KeyStore trusted = KeyStore.getInstance("BKS");
        // Get the raw resource, which contains the keystore with
        // your trusted certificates (root and any intermediate certs)
        InputStream in = getApplicationContext().getResources().openRawResource(R.raw.keystore);
        try {
            // Initialize the keystore with the provided trusted certificates
            // Provide the password of the keystore
            trusted.load(in, KEYSTORE_PASSWORD);
        } finally {
            in.close();
        }

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(trusted);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        SSLSocketFactory sf = context.getSocketFactory();
        return sf;
    } catch (Exception e) {
        throw new AssertionError(e);
    }
}
