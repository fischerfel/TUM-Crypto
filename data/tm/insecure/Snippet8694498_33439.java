    import javax.net.ssl.SSLContext;
    import javax.net.ssl.TrustManager;
    import javax.net.ssl.X509TrustManager;
    import org.apache.http.conn.ssl.SSLSocketFactory;
    {...}

        SSLContext sslContext = SSLContext.getInstance("SSL");

        // set up a TrustManager that trusts everything
        sslContext.init(null, new TrustManager[] { new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                            System.out.println("getAcceptedIssuers =============");
                            return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs,
                                    String authType) {
                            System.out.println("checkClientTrusted =============");
                    }

                    public void checkServerTrusted(X509Certificate[] certs,
                                    String authType) {
                            System.out.println("checkServerTrusted =============");
                    }
        } }, new SecureRandom());

        SSLSocketFactory sf = new SSLSocketFactory(sslContext);
        Scheme httpsScheme = new Scheme("https", sf, 443);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(httpsScheme);

        HttpParams params = new BasicHttpParams();
        ClientConnectionManager cm = new SingleClientConnManager(params, schemeRegistry);

        //DefaultHttpClient httpclient = new DefaultHttpClient();
        DefaultHttpClient httpclient = new DefaultHttpClient(cm, params);
