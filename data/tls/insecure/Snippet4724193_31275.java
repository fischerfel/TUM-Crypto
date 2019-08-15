val cleaner = Executors.newScheduledThreadPool(1) 
private val client = {
    val ssl_ctx = SSLContext.getInstance("TLS")
    val managers = Array[TrustManager](TrustingTrustManager)
    ssl_ctx.init(null, managers, new java.security.SecureRandom())
    val sslSf = new org.apache.http.conn.ssl.SSLSocketFactory(ssl_ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
    val schemeRegistry = new SchemeRegistry()
    schemeRegistry.register(new Scheme("https", 443, sslSf))
    val connection = new ThreadSafeClientConnManager(schemeRegistry)
    object clean extends Runnable{ 
        override def run = {
            connection.closeExpiredConnections
            connection.closeIdleConnections(30, SECONDS)
        }
    }
    cleaner.scheduleAtFixedRate(clean,10,10,SECONDS)
    val httpClient = new DefaultHttpClient(connection)
    httpClient.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY), new UsernamePasswordCredentials(username,password))
    httpClient
}
val get = new HttpGet(uri)
val entity = client.execute(get).getEntity
val stream = entity.getContent
val justForTheExample = IOUtils.toString(stream)
stream.close()
