    public void sendRequestWithCertificate(HttpRequestEnums type, String value) {

    KeyStore keyStore = null;

    String result = null;
    HttpURLConnection urlConnection = null;
    try {
        keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(getClientCertFile(), CERTIFICATE_PASSWORD.toCharArray());

        final TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(),
                new java.security.SecureRandom());


        URL requestedUrl = new URL(getUrl(type, value));

        urlConnection = (HttpURLConnection) requestedUrl.openConnection();

        String userCredentials = "username:password";
        final String basicAuth = "Basic " + Base64.encodeToString((userCredentials).getBytes(), Base64.NO_WRAP);
        urlConnection.setRequestProperty("Authorization", basicAuth);


        if (urlConnection instanceof HttpsURLConnection) {
            ((HttpsURLConnection) urlConnection)
                    .setSSLSocketFactory(sslContext.getSocketFactory());
        }

        urlConnection.setRequestMethod("GET");
        urlConnection.setConnectTimeout(CONN_TIMEOUT);
        urlConnection.setReadTimeout(CONN_TIMEOUT);

        int lastResponseCode = urlConnection.getResponseCode();
        result = IOUtil.readFully(urlConnection.getInputStream());
        String lastContentType = urlConnection.getContentType();
    } catch (KeyStoreException e) {
        e.printStackTrace();
    } catch (CertificateException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (KeyManagementException e) {
        e.printStackTrace();
    } catch (Error e) {
        e.printStackTrace();
    } finally {
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
    }
}
