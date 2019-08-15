    SSLSocketFactory sslSocketFactory=SSLSocketFactory.getSocketFactory();
    HostnameVerifier hostnameVerifier=org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
    sslSocketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);

    SchemeRegistry schemeRegistry = new SchemeRegistry();
    schemeRegistry.register(
            new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    schemeRegistry.register(
            new Scheme("https", sslSocketFactory, 443));
