class NullHostNameVerifier implements HostnameVerifier {
    @Override   
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
}

protected void testRequest(final String uri) {
    new AsyncTask<Void, Void, Void>() {     
        protected void onPreExecute() {
        }

        protected Void doInBackground(Void... params) {
            try {                   
                URL url = new URL("https://www.ssllabs.com/ssltest/viewMyClient.html");

                try {
                    sslContext = SSLContext.getInstance("TLS");
                    sslContext.init(null,
                        new X509TrustManager[] { new X509TrustManager() {
                            @Override
                            public void checkClientTrusted( final X509Certificate[] chain, final String authType ) {
                            }
                            @Override
                            public void checkServerTrusted( final X509Certificate[] chain, final String authType ) {
                            }
                            @Override
                            public X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }
                        } },
                        new SecureRandom());
                } catch (Exception e) {

                }

                HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

                conn.setSSLSocketFactory(sslContext.getSocketFactory());
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "Android");

                // Consume the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
        }
    }.execute();        
}
