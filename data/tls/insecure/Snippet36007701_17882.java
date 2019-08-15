public static void allowAllSSL() {

HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

javax.net.ssl.SSLContext context = null;
if (trustManagers == null) {
trustManagers = new TrustManager[] { new _FakeX509TrustManager() };
}
try {
context = javax.net.ssl.SSLContext.getInstance("TLS");
context.init(null, trustManagers, new SecureRandom());
} catch (NoSuchAlgorithmException e) {

} catch (KeyManagementException e) {

}
HttpsURLConnection.setDefaultSSLSocketFactory(context
.getSocketFactory());
}
