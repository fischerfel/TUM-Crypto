private SSLSocketFactory createSslSocketFactory() throws Exception
{
    TrustManager[] byPassTrustManagers = new TrustManager[]{new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }
    }};
    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, byPassTrustManagers, new SecureRandom());
    return sslContext.getSocketFactory();
}



try
{
    URL url = new URL(strings[0]);
    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
    SSLSocketFactory sslSocketFactory = createSslSocketFactory();
    urlConnection.setSSLSocketFactory(sslSocketFactory);
    urlConnection.setConnectTimeout(FrameUpdateApplication.CONNECTION_TIME_OUT);
    urlConnection.setReadTimeout(FrameUpdateApplication.READ_TIME_OUT_TERMSNSERVICES);
    urlConnection.connect();

    responseCode = urlConnection.getResponseCode();

    if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
        InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();

        String inputString;
        while ((inputString = bufferedReader.readLine()) != null) {
            builder.append(inputString);
        }
        JSONObject topLevel = new JSONObject(builder.toString());
        boolean statuss = topLevel.optBoolean("status");
        if (statuss) {
            String terms_service_text = topLevel.optString("data");
            if (!terms_service_text.isEmpty() && terms_service_text.length() > 0) {
                response = terms_service_text;
            }
        }
        isApiResponseError = false;
        urlConnection.disconnect();
    } else if (responseCode == HttpsURLConnection.HTTP_BAD_GATEWAY) {
        isApiResponseError = true;
        response = null;
    } else if (responseCode == HttpsURLConnection.HTTP_BAD_REQUEST) {
        isApiResponseError = true;
        response = null;
    } else if (responseCode == HttpsURLConnection.HTTP_GATEWAY_TIMEOUT) {
        isApiResponseError = true;
        response = null;
    } else if (responseCode == HttpsURLConnection.HTTP_INTERNAL_ERROR) {
        isApiResponseError = true;
        response = null;
    } else if (responseCode == HttpsURLConnection.HTTP_UNAVAILABLE) {
        isApiResponseError = true;
        response = null;
    } else if (responseCode == HttpsURLConnection.HTTP_CLIENT_TIMEOUT) {
        isApiResponseError = true;
        response = null;
    } else if (responseCode == HttpsURLConnection.HTTP_NOT_FOUND) {
        isApiResponseError = true;
        response = null;
    } else {
        response = null;
    }
} catch (MalformedURLException e) {
    e.printStackTrace();
    response = null;
} catch (ConnectException e) {
    ifSlowNetworkDetected = true;
    e.printStackTrace();
    response = null;
} catch (TimeoutException e) {
    ifSlowNetworkDetected = true;
    e.printStackTrace();
    response = null;
} catch (UnknownHostException e) {
    ifSlowNetworkDetected = true;
    e.printStackTrace();
    response = null;
} catch (IOException e) {
    ifSlowNetworkDetected = true;
    e.printStackTrace();
    response = null;
} catch (JSONException e) {
    e.printStackTrace();
    response = null;
} catch (Exception e) {
    e.printStackTrace();
    response = null;
}
return response;
