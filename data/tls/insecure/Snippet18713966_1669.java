/**
 * Trust only CAcert's CA. CA cert is injected as byte[]. Following best practices from
 * https://developer.android.com/training/articles/security-ssl.html#UnknownCa
 */
private static void trustCAcert() {
    try {
        // Load CAs from an InputStream
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        ByteArrayInputStream is = new ByteArrayInputStream(CACERTROOTDER);

        Certificate ca;
        try {
            ca = cf.generateCertificate(is);
            Log.d(TAG, "ca=", ((X509Certificate) ca).getSubjectDN());
        } finally {
            is.close();
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

        HttpsURLConnection.setDefaultSSLSocketFactory(
            sslContext.getSocketFactory());

        // added for testing only
        URL u = new URL(
            "https://myremoteapiurlsignedwiththesamecert.com/v1/doc.html");
        HttpsURLConnection con = (HttpsURLConnection) u.openConnection();
        con.setSSLSocketFactory(sslContext.getSocketFactory());
        BufferedReader r = new BufferedReader(
            new InputStreamReader(
                con.getInputStream())); // the exception is thrown here
        // because verification fails
        String l;
        while ((l = r.readLine()) != null) {
            Log.d(TAG, "l: ", l);
        }
    } catch (IOException e) { // none of the exceptions is thrown during setup
        Log.e(TAG, "IOException", e);
    } catch (CertificateException e) {
        Log.e(TAG, "CertificateException", e);
    } catch (NoSuchAlgorithmException e) {
        Log.e(TAG, "NoSuchAlgorithmException", e);
    } catch (KeyStoreException e) {
        Log.e(TAG, "KeyStoreException", e);
    } catch (KeyManagementException e) {
        Log.e(TAG, "KeyManagementException", e);
    }
}
