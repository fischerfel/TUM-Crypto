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
Scheme httpsScheme = new Scheme("https", 443, sf);
SchemeRegistry schemeRegistry = new SchemeRegistry();
schemeRegistry.register(httpsScheme);

// apache HttpClient version >4.2 should use BasicClientConnectionManager
ClientConnectionManager cm = new SingleClientConnManager(schemeRegistry);
HttpClient httpClient = new DefaultHttpClient(cm);
