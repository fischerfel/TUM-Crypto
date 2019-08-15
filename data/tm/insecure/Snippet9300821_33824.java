private void process() throws Exception {

    char[] pass = "clientpass".toCharArray();

    InputStream ksStream = getAssets().open("clientKeyStore.bks");
    KeyStore keyStore = KeyStore.getInstance("BKS");
    keyStore.load(ksStream, pass);
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(keyStore, pass);
    ksStream.close();

    X509TrustManager[] tm = new X509TrustManager[] { new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    } };

    SSLContext context = SSLContext.getInstance("TLS");
    context.init(kmf.getKeyManagers(), tm, null);
    HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    });

    URL url = new URL("https://192.168.2.101:8443/RestTomcat/resources/veiculos/KKK1234");
    HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
    BufferedReader br = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
    StringBuilder sb = new StringBuilder();
    String line = null;
    while ((line = br.readLine()) != null)
        sb.append(line + "\n");
    br.close();

    Log.e("OUTPUT", sb.toString());
    httpsURLConnection.disconnect();
}
