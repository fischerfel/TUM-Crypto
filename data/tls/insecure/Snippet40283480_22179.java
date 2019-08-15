    String[] result = { "", "" };
    try {

        HttpsURLConnection urlConnection = getConnection1(Url, context);
        urlConnection.setRequestMethod("GET");
        urlConnection.setConnectTimeout(connection_timeout);
        urlConnection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        if (Build.VERSION.SDK_INT > 13) {
            urlConnection.setRequestProperty("Connection", "close");
        }

        // urlConnection.setUseCaches(false);
        // urlConnection.setDoInput(true);
        // urlConnection.setDoOutput(true);

        urlConnection.connect();
        // always check HTTP response code first
        int responseCode = urlConnection.getResponseCode();
        result[0] = responseCode + "";

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Get Response
            InputStream is = urlConnection.getInputStream();
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            if (!TextUtils.isEmpty(response)) {
                result[0] = HttpConnectionUrl.RESPONSECODE_REQUESTSUCCESS;
                result[1] = response.toString();
                Log.i("TAG_RESPONSE : ", result[1]);
            }

        }

    } catch (UnsupportedEncodingException e) {
        result[0] = HttpConnectionUrl.RESPONSECODE_CONNECTIONTIMEOUT;
        e.printStackTrace();
    } catch (ClientProtocolException e) {
        result[0] = HttpConnectionUrl.RESPONSECODE_CONNECTIONTIMEOUT;
        e.printStackTrace();
    } catch (ConnectTimeoutException e) {
        result[0] = HttpConnectionUrl.RESPONSECODE_CONNECTIONTIMEOUT;
        e.printStackTrace();
    } catch (IOException e) {
        result[0] = HttpConnectionUrl.RESPONSECODE_CONNECTIONTIMEOUT;
        e.printStackTrace();
    } catch (Exception e) {
        result[0] = HttpConnectionUrl.RESPONSECODE_CONNECTIONTIMEOUT;
        e.printStackTrace();
    }
    return result;
}

public static HttpsURLConnection getConnection1(String apivalue,
        Context mContext) throws IOException {
    try {
        URL url = new URL(apivalue);
        HttpsURLConnection urlConnection = (HttpsURLConnection) url
                .openConnection();
        KeyStore trustStore = KeyStore.getInstance("BKS");
        InputStream trustStoreStream = mContext.getResources()
                .openRawResource(R.raw.qamcorp);
        trustStore.load(trustStoreStream, "123456".toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory
                .getInstance(KeyManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
        SSLSocketFactory factory = sslContext.getSocketFactory();
        SSLSocket socket = (SSLSocket) factory.createSocket();
        socket.setEnabledCipherSuites(SSLUtils
                .getCipherSuitesWhiteList(socket.getEnabledCipherSuites()));
        urlConnection.setSSLSocketFactory(factory);

        return urlConnection;
    } catch (GeneralSecurityException e) {

        throw new IOException("Could not connect to SSL Server", e);
    }
}
