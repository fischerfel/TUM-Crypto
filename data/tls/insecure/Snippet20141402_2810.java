    SSLContext sc = SSLContext.getInstance("SSL");
    TrustAllCertificates trustAll = new TrustAllCertificates();
    sc.init(null, new TrustManager[] { trustAll }, new java.security.SecureRandom());

    URL url = new URL("https://www.google.com/");
    URLConnection urlConnection = url.openConnection();
    if (urlConnection instanceof HttpsURLConnection) {
        HttpsURLConnection uc = (HttpsURLConnection) urlConnection;

        uc.setSSLSocketFactory(sc.getSocketFactory());
        uc.setHostnameVerifier(trustAll);

        uc.connect();
        JatLog.writeTempLog("headers: "+uc.getHeaderFields());
        uc.disconnect();
    }
