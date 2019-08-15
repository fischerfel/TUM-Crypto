conn.setHostnameVerifier(new HostnameVerifier() {

    @Override
    public boolean verify(String hostname, SSLSession session) {
        String host  = "www.test.com";

        return HttpsURLConnection.getDefaultHostnameVerifier().verify(host, session);
    }
});
