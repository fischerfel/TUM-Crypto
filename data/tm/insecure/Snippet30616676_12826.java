private static final String TLS_PROTOCOL_1_2 = "TLSv1.2";
    private static final String TLS_PROTOCOL_1_1 = "TLSv1.1";
    private static final String TLS_PROTOCOL_3 = "SSLv3";
    private static final String TLS_RSA_WITH_AES_256_CBC_SHA ="TLS_RSA_WITH_AES_256_CBC_SHA";
    private static final String TLS_RSA_WITH_AES_256_CBC_SHA256 ="TLS_RSA_WITH_AES_256_CBC_SHA256";
    private static final String TLS_RSA_WITH_AES_256_GCM_SHA384 = "TLS_RSA_WITH_AES_256_GCM_SHA384";
    private static final String TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA = "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA";
    private static final String TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384 = "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384";
    private static final String AUTHORIZATION = "Basic Zmlkb3I6d2lyIWJhbmsk";

    @Override
    public HttpURLConnection openSecureConnection(String path) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        URL url = new URL(baseUrl+path);
        SSLContext sslContext = SSLContext.getInstance(TLS_PROTOCOL_1_2);

        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                }
        };

        sslContext.init(null, trustAllCerts, new  java.security.SecureRandom());
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

        con.setSSLSocketFactory(sslSocketFactory);
        con.setDoOutput(true);
        con.setConnectTimeout(getTimeout());
        con.setReadTimeout(getTimeout());
        //set server-prefered cipher suits
        SSLServerSocket soc = (SSLServerSocket)sslContext.getServerSocketFactory().createServerSocket();
        soc.setEnabledProtocols(new String[]{TLS_PROTOCOL_3, TLS_PROTOCOL_1_2, TLS_PROTOCOL_1_1});
        soc.setEnabledCipherSuites(new String[] {
                TLS_RSA_WITH_AES_256_CBC_SHA,
                TLS_RSA_WITH_AES_256_CBC_SHA256,
                TLS_RSA_WITH_AES_256_GCM_SHA384,
                TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
                TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384
        });

        return con;
    }
