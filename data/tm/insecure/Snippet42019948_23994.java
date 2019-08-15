public class HTTPClient {

public static OkHttpClient getUnsafeOkHttpClient() {
    try {
        // Create a trust manager that does not validate certificate chains

        final TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("TLSv1");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        // Create a ssl socket factory with our all-trusting manager
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        //URL url = new URL(ApiIntentService.getHostAddress());
        //final SSLSocketFactory sslSocketFactory = new NoSSLv3SocketFactory(url);

//            ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
//                    .tlsVersions(TlsVersion.TLS_1_0)
//                    .cipherSuites(
//                            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
//                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
//                            CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
//                            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
//                            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
//                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
//                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
//                            CipherSuite.TLS_ECDHE_ECDSA_WITH_RC4_128_SHA,
//                            CipherSuite.TLS_ECDHE_RSA_WITH_RC4_128_SHA,
//                            CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA)
//                    .build();

//            String hostname = "api.server.domain";
//            CertificatePinner certificatePinner = new CertificatePinner.Builder()
//                    .add(hostname, "sha256/3Iiwgs3a0qjPCnBQzW/GeHhPbZvhaJtxKvMJJVO5KdU=")
//                    .build();

        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
//            builder.connectionSpecs(Collections.singletonList(spec));
//            builder.certificatePinner(certificatePinner);
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            builder.authenticator(new Authenticator() {
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    String credential = Credentials.basic("user", "pass");
                    return response.request().newBuilder()
                            .header("Authorization", credential)
                            .build();
                }
            });
        OkHttpClient okHttpClient = builder.build();
        return okHttpClient;
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
