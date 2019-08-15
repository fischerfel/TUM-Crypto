public void UseHttpsConnection(String url, String charset, String query) {

    try {
        final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
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
        } };

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance( "TLS" );
        sslContext.init( null, trustAllCerts, new java.security.SecureRandom() );
        // Create an ssl socket factory with our all-trusting manager
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();



        System.setProperty("http.keepAlive", "false");
        HttpsURLConnection connection = (HttpsURLConnection) new URL(url)
                .openConnection();
        connection.setSSLSocketFactory( sslSocketFactory );
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Charset", charset);
        connection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded;charset=" + charset);
        OutputStream output = null;
        try {
            output = connection.getOutputStream();
            output.write(query.getBytes(charset));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null)
                try {
                    output.close();
                } catch (IOException logOrIgnore) {
                    logOrIgnore.printStackTrace();
                }
        }

        int status = ((HttpsURLConnection) connection).getResponseCode();
        Log.i("", "Status : " + status);

        for (Entry<String, List<String>> header : connection
                .getHeaderFields().entrySet()) {
            Log.i("Headers",
                    "Headers : " + header.getKey() + "="
                            + header.getValue());
        }

        InputStream response = new BufferedInputStream(
                connection.getInputStream());

        int bytesRead = -1;
        byte[] buffer = new byte[30 * 1024];
        while ((bytesRead = response.read(buffer)) > 0) {
            byte[] buffer2 = new byte[bytesRead];
            System.arraycopy(buffer, 0, buffer2, 0, bytesRead);
            handleDataFromSync(buffer2);
        }

        connection.disconnect();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
