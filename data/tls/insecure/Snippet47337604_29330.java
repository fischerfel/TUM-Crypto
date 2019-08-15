public static void changeHealthState(final String token) throws NoSuchAlgorithmException, KeyManagementException, IOException {
    final TrustManager[] trustManager = new TrustManager[] { new X509TrustManager() {
        public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
        }

        public void checkServerTrusted(final X509Certificate[] chain, final String authType) {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }};

    SSLContext sslContext;
    sslContext = SSLContext.getInstance("SSL");
    sslContext.init(null, trustManager, new java.security.SecureRandom());

    final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

    Authenticator.setDefault(new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(token, "".toCharArray());
        }
    });

    final URLConnection urlConnection = new URL(
            "myurl").openConnection();

    ((HttpsURLConnection) urlConnection).setSSLSocketFactory(sslSocketFactory);
    int responseCode = ((HttpsURLConnection) urlConnection).getResponseCode();
    StringBuffer result = new StringBuffer();

    final InputStream input;

    BufferedReader bufferedReader;

    if (responseCode == 200) {
        input = urlConnection.getInputStream();
    } else {
        input = null;
    }

    bufferedReader = new BufferedReader(new InputStreamReader((input)));

    String line = "";

    while ((line = bufferedReader.readLine()) != null) {
        result.append(line);
    }
    String output = null;
    output = result.toString();
    System.out.println(output);
    input.close();
}
