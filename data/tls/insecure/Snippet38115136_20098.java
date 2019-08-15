    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, MalformedURLException, IOException {
    System.setProperty("https.protocols", "TLSv1,SSLv2Hello");

    String url = "https://www.google.com";

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, new TrustManager[]{new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] xcs, String string) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] xcs, String string) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }}, new SecureRandom());

    SSLSocketFactory socketFactory = sslContext.getSocketFactory();
    HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);

    HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
    conn.setHostnameVerifier(new HostnameVerifier() {
        @Override
        public boolean verify(String string, SSLSession ssls) {
            return true;
        }
    });

    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

    Stream<String> lines = br.lines();
    lines.forEach(l -> { System.out.println(l);});
}
