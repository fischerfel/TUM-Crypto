private SSLSocketFactory newSslSocketFactory() {
    try {
        // Get an instance of the Bouncy Castle KeyStore format
        KeyStore trusted = KeyStore.getInstance("BKS");
        // Get theraw resource, which contains the keystore with
        // your trusted certificates (root and any intermediate certs)
        Context context = this.activity;
        Resources _resources = context.getResources();
        InputStream in = _resources.openRawResource(R.raw.mystore);
        try {
            // Initialize the keystore with the provided trusted certificates
            // Also provide the password of the keystore
            trusted.load(in, "mypassword".toCharArray());
        } finally {
            in.close();
        }
        // Pass the keystore to the SSLSocketFactory. The factory is responsible
        // for the verification of the server certificate.
        SSLSocketFactory sf = new SSLSocketFactory(trusted);
        // Hostname verification from certificate

        sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
        return sf;
    } catch (Exception e) {
        throw new AssertionError(e);
    }
}
