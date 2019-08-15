        trustAllHosts();
        urlConnection = (HttpsURLConnection) url.openConnection();


        urlConnection.setHostnameVerifier(DO_NOT_VERIFY);
        urlConnection.setReadTimeout(CONNECT_TIMEOUT);
        urlConnection.setConnectTimeout(CONNECT_TIMEOUT / 2);

        //urlConnection.connect();
        int respCode = urlConnection.getResponseCode();
        if (respCode >= 400) {
            if (respCode == 404 || respCode == 410) {
                throw new FileNotFoundException(url.toString());
            } else {
                throw new java.io.IOException(
                    "Server returned HTTP"
                    + " response code: " + respCode
                    + " for URL: " + url.toString());
            }

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                urlConnection.getInputStream()), 1000);
