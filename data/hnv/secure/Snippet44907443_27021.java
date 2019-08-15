    def httpBuilder = new HTTPBuilder("https://www.hitmeister.de/api/v1")
    //httpBuilder.ignoreSSLIssues()

    def keyStore = KeyStore.getInstance(KeyStore.defaultType)
    getClass().getResource( "/javakeystore.jks").withInputStream {
        keyStore.load(it, "******".toCharArray())
        println "keystore load..."
    }
    SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore)
    socketFactory.setHostnameVerifier( new AllowAllHostnameVerifier())
    httpBuilder.client.connectionManager.schemeRegistry.register(
            new Scheme("https", socketFactory, 443 ) )
    //httpBuilder.client.connectionManager.schemeRegistry.register(new Scheme('https',443,socketFactory))
    println "GELDI>>>"
    def response = httpBuilder.request(GET, ContentType.BINARY) {

        uri.path = '/categories/?limit=20'
        headers.'accept' = "application/json"
        headers.'hm-client' = params.authentication.apiKey
        headers.'hm-signature' = signature
        headers.'hm-timestamp' = timestamp
    }
