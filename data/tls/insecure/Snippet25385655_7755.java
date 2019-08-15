public static void main(String[] args) throws NoSuchAlgorithmException,
        KeyManagementException, IOException {

    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certs,
                String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs,
                String authType) {
        }
    } };

    final SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

    HostnameVerifier allHostsValid = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

    URL url = new URL("https://example.com:13902");
    URLConnection con = url.openConnection();
    final Reader reader = new InputStreamReader(con.getInputStream());
    final BufferedReader br = new BufferedReader(reader);
    String line = "";
    while ((line = br.readLine()) != null) {
        System.out.println(line);
    }
    br.close();
}
