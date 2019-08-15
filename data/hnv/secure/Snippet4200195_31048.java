    private SSLSocketFactory newSslSocketFactory() {
    try {
        // Get an instance of the Bouncy Castle KeyStore format
        KeyStore trusted = KeyStore.getInstance("BKS");
        // Get the raw resource, which contains the keystore with
        // your trusted certificates (root and any intermediate certs)
        InputStream in = context.getResources().openRawResource(R.raw.mykeystore);
        try {
            // Initialize the keystore with the provided trusted certificates
            // Also provide the password of the keystore
            trusted.load(in, "testtest".toCharArray());
        } finally {
            in.close();
        }
        // Pass the keystore to the SSLSocketFactory. The factory is responsible
        // for the verification of the server certificate.
        SSLSocketFactory sf = new SSLSocketFactory(trusted);
        // Hostname verification from certificate
        // http://hc.apache.org/httpcomponents-client-ga/tutorial/html/connmgmt.html#d4e506
        sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
        return sf;
    } catch (Exception e) {
        throw new AssertionError(e);
    }
}
