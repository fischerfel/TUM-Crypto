        HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
        final SSLSocketFactory socketFactory = SSLSocketFactory
                .getSocketFactory();
        socketFactory
                .setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", socketFactory, 443));
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

        HttpClient client = new DefaultHttpClient(params);
