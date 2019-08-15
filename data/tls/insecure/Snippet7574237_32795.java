    public static URLConnection CreateFromP12(String uri, String keyFilePath,
        String keyPass, TrustManager[] trustPolicy, HostnameVerifier hv) {
    try {

        SSLContext sslContext = SSLContext.getInstance("TLS");
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");

        keyStore.load(new FileInputStream(keyFilePath),
                keyPass.toCharArray());

        kmf.init(keyStore, keyPass.toCharArray());
        sslContext.init(kmf.getKeyManagers(), trustPolicy, null);

        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
                .getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    } catch (Exception ex) {
        return null;
    }

    URL url;
    URLConnection conn;
    try {
        url = new URL(uri);
        conn = url.openConnection();
    } catch (MalformedURLException e) {
        return null;
    } catch (IOException e) {
        return null;
    }

    return conn;
}
