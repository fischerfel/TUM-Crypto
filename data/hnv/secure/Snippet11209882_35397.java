    String urlString = "http://USERNAME:PASSWORD@SomeAdress/u/db2.php";
    String params = "version=20110516140000";
    InputStream stream = null;
    url = new URL(urlString);
    if (url.getProtocol().toLowerCase().equals("https")) {
        trustAllHosts();

        HttpsURLConnection urlconn = (HttpsURLConnection) url.openConnection();
        urlconn.setHostnameVerifier(DO_NOT_VERIFY);
        urlconn.setDoInput(true);
        urlconn.setDoOutput(true);
        urlconn.setRequestMethod("POST");
        urlconn.setRequestProperty("Content-Type", "text/xml");
        urlconn.setRequestProperty("Content-Length", String.valueOf(params.getBytes().length));

        OutputStreamWriter wr = new OutputStreamWriter(urlconn.getOutputStream());
        wr.write(params);
        wr.flush();

        stream = urlconn.getInputStream();
        if ("gzip".equals(urlconn.getContentEncoding())) {
            stream = new GZIPInputStream(stream);
        }

        wr.close();

    } else {            
        // Almost the same as if-path, but without trustAllHosts() and with HttpURLConnection instead of HttpsURLConnection         
    }

    return stream;
