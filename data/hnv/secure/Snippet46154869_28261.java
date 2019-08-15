public HttpURLConnection createHttpUrlConnection(URL url) throws IOException {
        Log.d(TAG, "Create http url connection with url : " + url.toString());

        HttpURLConnection httpConnection = null;
        if ("https".equalsIgnoreCase(url.getProtocol())) {
            HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
            https.setHostnameVerifier(DO_NOT_VERIFY);
            httpConnection = https;
        } else {
            httpConnection = (HttpURLConnection) url.openConnection();
        }

        httpConnection.setReadTimeout(TIMEOUT);
        httpConnection.setConnectTimeout(TIMEOUT);
        String basicAuth = "Basic " + new String(Base64.encode((AppConstants.USERNAME + ":" + AppConstants.PASSWORD).getBytes(), Base64.NO_WRAP));
        httpConnection.setRequestProperty("Authorization", basicAuth);
        httpConnection.setRequestProperty("Accept-Language", Locale.getDefault().getLanguage());

        return httpConnection;
    }
