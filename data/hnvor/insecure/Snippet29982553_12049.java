    private static final HostnameVerifier DUMMY_VERIFIER = new HostnameVerifier() {

    @Override
    public boolean verify(String hostname, SSLSession session) {

        return true;
    }
};

    public static String method(String url)
        throws Exception {

    // Trust manager to accept all certificates..
    TrustManager localTrustManager = new X509TrustManager() {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain,
                String authType) throws CertificateException {

        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain,
                String authType) throws CertificateException {

        }

    };

    HttpsURLConnection con = null;

    try {
        // URL object is built..
        URL urlObj = new URL(url);

        SSLContext sslc = SSLContext.getInstance("TLS");

        sslc.init(null, new TrustManager[] { localTrustManager },
                new SecureRandom());

        HttpsURLConnection.setDefaultSSLSocketFactory(sslc
                .getSocketFactory());

        // Connection is established..
        con = (HttpsURLConnection) urlObj.openConnection();
        con.setHostnameVerifier(DUMMY_VERIFIER);
        con.setRequestMethod("POST");

        con.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");

        con.setUseCaches(false);
        con.setDoOutput(true);

        DataOutputStream writer_stream = new DataOutputStream(
                con.getOutputStream());

        // for sending info to server..
        writer_stream.writeBytes(new_request);
        writer_stream.flush();
        writer_stream.close();

        // Get Response from the server..
        InputStream is = con.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        String line;
        StringBuffer response = new StringBuffer();

        while ((line = rd.readLine()) != null) {

            response.append(line);
            response.append('\r');
        }
        rd.close();

        return response.toString();

    } catch (Exception e) {

        e.printStackTrace();
        return null;

    } finally {

        if (con != null) {
            con.disconnect();
        }
    }
}
