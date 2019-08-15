    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

    StrictMode.setThreadPolicy(policy);

     // RESTORE THE COOKIES!!!

    cookieJar = new BasicCookieStore();
    localContext = new BasicHttpContext();
    localContext.setAttribute(ClientContext.COOKIE_STORE, cookieJar);

    cookie = extras.getString("cookies");
    String[] cookieCutter = cookie.split(";");
    for (int i=0; i < cookieCutter.length; i++)
    {
        String[] values = cookieCutter[i].split("=");
        BasicClientCookie c = new BasicClientCookie(values[0], values[1]);
        c.setDomain(MAIL_WEB_SERVER);
        cookieJar.addCookie((Cookie)c);
    }
    HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;


    // Create local HTTP context


    SchemeRegistry registry = new SchemeRegistry();
    SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
    socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
    registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    registry.register(new Scheme("https", socketFactory, 443));
    HttpParams params = new BasicHttpParams();
    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
    HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
    ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager(params, registry);

    httpClient = new DefaultHttpClient(mgr, params);
    httpClient.setCookieStore(cookieJar);

    // Set verifier     
    HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
