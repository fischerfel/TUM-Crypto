        URL url = new URL(SSL_URL);
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setSSLSocketFactory(context.getSocketFactory());
        urlConnection.setHostnameVerifier(hostnameVerifier);
        urlConnection.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
        urlConnection.connect();
        OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
        if (out != null)
        {
           out.write(getReqData());
           out.flush();
           out.close();
        }
        int res = urlConnection.getResponseCode();
        String message = urlConnection.getResponseMessage();
        InputStream in = urlConnection.getInputStream();
        String msg = convertStreamToString(in);
