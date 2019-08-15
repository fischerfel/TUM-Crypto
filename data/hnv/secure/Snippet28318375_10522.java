    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    if (conn instanceof HttpsURLConnection) {
        HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
        httpsConn.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
        httpsConn.setHostnameVerifier(new AllowAllHostnameVerifier());
    }
