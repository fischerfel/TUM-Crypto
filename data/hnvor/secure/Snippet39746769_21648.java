HttpsURLConnection urlConnection = setUpHttpsConnection(url.toString());

    try {
        urlConnection.setRequestMethod("POST");
    } catch (ProtocolException e) {
        e.printStackTrace();
    }
    urlConnection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");

    urlConnection.setHostnameVerifier(new HostnameVerifier() {

        @Override
        public boolean verify(String s, SSLSession sslSession) {
            HostnameVerifier hv =
                    HttpsURLConnection.getDefaultHostnameVerifier();
            return hv.verify("my.hostname.com.tr", sslSession);
        }

    });
