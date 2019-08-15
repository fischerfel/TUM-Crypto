String truststore = "D://certificate//latest//client-pkcs-12-cert";
char truststorepass[] = "keypass".toCharArray();
KeyStore ks = KeyStore.getInstance("pkcs12");
ks.load(new FileInputStream(truststore), truststorepass);

TrustManagerFactory tmf = TrustManagerFactory
        .getInstance("SunX509");

tmf.init(ks);
SSLContext ctx = SSLContext.getInstance("TLSv1");
ctx.init(null, tmf.getTrustManagers(), null);
