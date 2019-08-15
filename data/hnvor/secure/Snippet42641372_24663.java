TrustManager[] trustAllCerts = new TrustManager[] {
    new X509TrustManager() {

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
    }
};

// Install the all-trusting trust manager
SSLContext sc = SSLContext.getInstance("SSL");
sc.init(null, trustAllCerts, new java.security.SecureRandom());
HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

// Create all-trusting host name verifier
HostnameVerifier hostnameVerifier = new HostnameVerifier() {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        HostnameVerifier hv =
        HttpsURLConnection.getDefaultHostnameVerifier();
        return hv.verify("something.com", session);
    }
};

URL url = new URL("Something");
urlConnection = (HttpsURLConnection) url.openConnection();
urlConnection.setHostnameVerifier(hostnameVerifier);
