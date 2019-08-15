implicit def sslContext: SSLContext = { 
    val context = SSLContext.getInstance("TLS") 
    context.init(null, Array[TrustManager](new DummyTrustManager), new SecureRandom())
    context
}
