def http = new groovyx.net.http.HTTPBuilder( 'https://localhost:8443/' )

//=== SSL UNSECURE CERTIFICATE ===
def sslContext = SSLContext.getInstance("SSL")
sslContext.init(null, [ new X509TrustManager() {
  public X509Certificate[] getAcceptedIssuers() {null }
  public void checkClientTrusted(X509Certificate[] certs, String authType) { }
  public void checkServerTrusted(X509Certificate[] certs, String authType) { }
} ] as TrustManager[], new SecureRandom())
def sf = new SSLSocketFactory(sslContext)
def httpsScheme = new Scheme("https", sf, 8443)
http.client.connectionManager.schemeRegistry.register( httpsScheme )

http.request( groovyx.net.http.Method.POST, groovyx.net.http.ContentType.JSON ) { req ->
    uri.path = '/a/admin/runtime/upload'
    uri.query = [ username: "username", password: "password", version: version]
    requestContentType = 'multipart/form-data'
    org.apache.http.entity.mime.MultipartEntity entity = new org.apache.http.entity.mime.MultipartEntity()
    def file = new File("file.zip")
    entity.addPart("runtimeFile", new org.apache.http.entity.mime.content.ByteArrayBody(file.getBytes(), 'file.zip'))
    req.entity = entity
}
