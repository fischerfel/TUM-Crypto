KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
KeyStore keyStore = KeyStore.getInstance("PKCS12");



InputStream is = new FileInputStream(new File("client.p12"));
keyStore.load(is,"ccCert".toCharArray());
is.close();
keyManagerFactory.init(keyStore,"ccCert".toCharArray());

SSLContext context = SSLContext.getInstance("TLS");

TrustManagerFactory trustManagerFactory = TrustManagerFactory
        .getInstance(TrustManagerFactory.getDefaultAlgorithm());
trustManagerFactory.init(keyStore);
context.init(keyManagerFactory.getKeyManagers(),trustManagerFactory.getTrustManagers(), new SecureRandom());


URL url = new URL("https://localhost:8787");
HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
con.setSSLSocketFactory(context.getSocketFactory());
con.connect();
Exception in thread "main" javax.net.ssl.SSLHandshakeException: sun.security.validator.ValidatorException: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target.
