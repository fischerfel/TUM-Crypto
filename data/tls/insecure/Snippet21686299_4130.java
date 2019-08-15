 import org.apache.camel.component.http4.HttpClientConfigurer;
 import org.apache.http.client.HttpClient;
 ...
 public class myHttpClientConfigurer implements HttpClientConfigurer {

    @Override
    public void configureHttpClient(HttpClient hc) {
        try {
            Properties properties = loadProperties();
            KeyStore trustStore = KeyStore.getInstance("JKS");
            final String javaKeystoreFile = getJavaKeystoreFile(properties);
            final String keystorePassword = getKeystorePassword(properties);
            trustStore.load(new FileInputStream(javaKeystoreFile), keystorePassword.toCharArray());

            KeyManagerFactory keyFactory = KeyManagerFactory.getInstance("SunX509");
            keyFactory.init(trustStore, keystorePassword.toCharArray());

            TrustManagerFactory trustFactory = TrustManagerFactory.getInstance("SunX509");
            trustFactory.init(trustStore);

            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(keyFactory.getKeyManagers(), trustFactory.getTrustManagers(), null);

            TrustStrategy trustStrategy = new TrustStrategy() {

                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }

            };

            SSLSocketFactory factory = new SSLSocketFactory(SSLSocketFactory.TLS, trustStore, keystorePassword, trustStore, null, trustStrategy, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            SchemeRegistry registry = hc.getConnectionManager().getSchemeRegistry();
            registry.register(new Scheme("https", 443, factory));

        catch ...
 }
