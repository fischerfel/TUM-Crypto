protected Bitmap doInBackground(String... params) {
    try {

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        HttpsURLConnection connection = (HttpsURLConnection)
                new URL("www.example.com.br").openConnection();
        connection.setHostnameVerifier(getHostnameVerifier("www.example.com.br");
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(3000);
        connection.setUseCaches(false);
        connection.setAllowUserInteraction(false);
        connection.connect();
        setCookieActivity(connection.getHeaderField("Set-Cookie"));
        return BitmapFactory.decodeStream(connection.getInputStream());
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}



private HostnameVerifier getHostnameVerifier(final String url) {
    HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            HostnameVerifier hv =
                    HttpsURLConnection.getDefaultHostnameVerifier();
            return hv.verify(url, session);
        }
    };
    return hostnameVerifier;
}
