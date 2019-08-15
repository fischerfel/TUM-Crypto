    def httpBuilder = new HTTPBuilder("https://www.hitmeister.de/api/v1")
    //httpBuilder.ignoreSSLIssues()

    def sslContext = SSLContext.getInstance("TL")
    sslContext.init(null, [new X509TrustManager() { public X509Certificate[] getAcceptedIssuers() {null}
            public void checkClientTrusted(X509Certificate[] certs, String authType) { }
            public void checkServerTrusted(X509Certificate[] certs, String authType) { }
        } ] as TrustManager[], new SecureRandom())
    def sf = new SSLSocketFactory(sslContext, org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
    //sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_V‌​ERIFIER)
    def httpsScheme = new Scheme("https", sf, 443)

    println "httpsSchme::::"+httpsScheme
    httpBuilder.client.connectionManager.schemeRegistry.register( httpsScheme )

    httpBuilder.request(GET) {
        headers.'accept' = "application/json"
        headers.'hm-client' = params.authentication.apiKey
        headers.'hm-signature' = signature
        headers.'hm-timestamp' = timestamp

    }
