public static void login()
{
try {
    KeyStore ks = KeyStore.getInstance("Windows-MY");
    ks.load(null, null);
    String kalg = KeyManagerFactory.getDefaultAlgorithm();
    System.out.println(kalg);
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(kalg);
    kmf.init(ks, null);
    String talg = TrustManagerFactory.getDefaultAlgorithm();
    System.out.println(talg);
    final TrustManager[] trustAllCerts = new TrustManager[] { new       X509TrustManager() {
        @Override
        public void checkClientTrusted(final X509Certificate[] chain,
                final String authType) {
        }

        @Override
        public void checkServerTrusted(final X509Certificate[] chain,
                final String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    } };


    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(kmf.getKeyManagers(), trustAllCerts, new java.security.SecureRandom());
    HttpsURLConnection
        .setDefaultSSLSocketFactory(sc.getSocketFactory());
    URL url = new URL("xxxxx");
    HttpsURLConnection httpsCon = (HttpsURLConnection) url
        .openConnection();
    InputStream is = httpsCon.getInputStream();
    httpsCon.getHeaderFields();
    String str =httpsCon.getHeaderField("Set-Cookie");
    System.out.println(httpsCon.getResponseMessage());
    int c;
    StringBuffer sb = new StringBuffer();
    while ((c = is.read()) >= 0) {
    System.out.print((char)c);
    sb.append((char) c);
    }
    is.close();
} catch (Exception ex) {
    ex.printStackTrace();
}
}
