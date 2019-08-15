    private class NetworkCommunication extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        String paramUrl = params[0];
        String paramRequest = params[1];

        HttpsURLConnection connection = null;

        TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };

        try {
            SSLContext sslcontext = SSLContext.getInstance("SSL");
            sslcontext.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
            Log.d("meet", "NetworkCommunication::setDefaultSSLSocketFactory()");

            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

            });
            Log.d("meet", "NetworkCommunication::setDefaultHostnameVerifier()");

            URL url = new URL(paramUrl);                
            connection = (HttpsURLConnection) url.openConnection();
            Log.d("meet", "NetworkCommunication::openConnection()");

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Length", String.valueOf(paramRequest.getBytes().length));
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            Log.d("meet", "NetworkCommunication::set...()");

            DataOutputStream output = new DataOutputStream(connection.getOutputStream());
            output.writeBytes(paramRequest);
            output.flush();
            output.close();
            Log.d("meet", "NetworkCommunication::DataOutputStream");


        } catch (Exception e) {
            Log.d("meet", "NetworkCommunication::Exception", e);
            return null;
        } finally {
            if(connection != null)
                connection.disconnect();
        }

        return null;
    }

}
