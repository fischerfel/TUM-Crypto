final HttpsURLConnection https = (HttpsURLConnection) requestedUrl.openConnection();

try {

    https.setConnectTimeout(timeout);
    https.setReadTimeout(timeout);
    https.setRequestMethod(method);

    if (sslSocketFactory != null) {
        https.setSSLSocketFactory(sslSocketFactory);
    }

    https.setHostnameVerifier(new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
            return hv.verify("localhost", session);
        }
    });
...
