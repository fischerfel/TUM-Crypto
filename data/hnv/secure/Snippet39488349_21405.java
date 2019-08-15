public String GetUnit(String url) {
        String result = null;
        HttpURLConnection urlConnection = null;
        try {
            URL requestedUrl = new URL(url);
            urlConnection = (HttpURLConnection) requestedUrl.openConnection();
            if (urlConnection instanceof HttpsURLConnection) {
                ((HttpsURLConnection) urlConnection).setSSLSocketFactory(sslContext.getSocketFactory());
                ((HttpsURLConnection) urlConnection).setHostnameVerifier(new BrowserCompatHostnameVerifier());
            }
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(1500);
            urlConnection.setReadTimeout(1500);
            lastResponseCode = urlConnection.getResponseCode();
            result = IOUtil.readFully(urlConnection.getInputStream());
        } catch (Exception ex) {
            result = ex.toString();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return result;
    }
