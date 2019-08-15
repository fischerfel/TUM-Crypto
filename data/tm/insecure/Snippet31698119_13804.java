SSLContext sslcontext = SSLContext.getInstance("TLS");
sslcontext.init(null, new TrustManager[] { new X509TrustManager() {
@Override
public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

@Override
public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

@Override
public java.security.cert.X509Certificate[] getAcceptedIssuers() {
return new X509Certificate[0];
}

} }, new java.security.SecureRandom());

Client client = ClientBuilder.newBuilder().sslContext(sslcontext).hostnameVerifier((s1, s2) -> true)
.register(MultiPartFeature.class)
.register(new EncodingFeature("gzip", GZipEncoder.class))
.build();
