TrustManager[] trustAllcerts = new TrustManager[]{
    new X509TrustManager() {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            // TODO Auto-generated method stub

        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            // TODO Auto-generated method stub

        }
    }};

javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
sc.init(null, trustAllcerts, new java.security.SecureRandom());
HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
HostnameVerifier allHostsValid = new HostnameVerifier() {

    @Override
    public boolean verify(String arg0, SSLSession arg1) {
        // TODO Auto-generated method stub
        return false;
    }
};
HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
