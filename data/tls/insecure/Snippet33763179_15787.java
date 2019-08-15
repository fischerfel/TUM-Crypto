private HttpsURLConnection buildSslServerConnection() {
    HttpsURLConnection urlConnection = null;
    try {
        // Load CAs from an InputStream
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = new BufferedInputStream(context.getAssets().open("ca.pem"));
        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
        } finally {
            caInput.close();
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
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        // Tell the URLConnection to use a SocketFactory from our SSLContext
        urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setSSLSocketFactory(context.getSocketFactory());
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Authorization", "Basic" + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT));
        urlConnection.setConnectTimeout(7000);
        urlConnection.setReadTimeout(7000);
        urlConnection.setInstanceFollowRedirects(false);
        urlConnection.setUseCaches(false);
        urlConnection.setAllowUserInteraction(false);
        urlConnection.setDoOutput(false);
    } catch (KeyManagementException e) {
        LOG.error("Error while checking server connectivity: ", e);
    } catch (CertificateException e) {
        LOG.error("Error while checking server connectivity: ", e);
    } catch (FileNotFoundException e) {
        LOG.error("Error while checking server connectivity: ", e);
    } catch (KeyStoreException e) {
        LOG.error("Error while checking server connectivity: ", e);
    } catch (NoSuchAlgorithmException e) {
        LOG.error("Error while checking server connectivity: ", e);
    } catch (MalformedURLException e) {
        LOG.error("Error while checking server connectivity: ", e);
    } catch (IOException e) {
        LOG.error("Error while checking server connectivity: ", e);
    }
    return urlConnection;
}
