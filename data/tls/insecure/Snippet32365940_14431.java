serverHttps = HttpsServer.create(new InetSocketAddress(ports[port_selector]), 0);
SSLContext sslContext = SSLContext.getInstance("TLS");

String alias = "alias";

// Load Certificates
InputStream stream = MyClass.class.getResourceAsStream("/certs/mycert.crt");
CertificateFactory cf = CertificateFactory.getInstance("X.509");
X509Certificate cert = (X509Certificate)cf.generateCertificate(stream);
stream.close();

stream = MyClass.class.getResourceAsStream("/certs/bundle.crt");
cf = CertificateFactory.getInstance("X.509");
Collection bundle = cf.generateCertificates(stream);
stream.close();

// Build cert chain
java.security.cert.Certificate[] chain = new Certificate[bundle.size()+1];
Iterator i = bundle.iterator();
int pos = 0;
while (i.hasNext()) {
    chain[pos] = (Certificate)i.next();
    pos++;
}
chain[chain.length-1] = cert;

// Load private key
KeyFactory keyFactory = KeyFactory.getInstance("RSA");
stream = MyClass.class.getResourceAsStream("/certs/pkcs8_my_key");
PKCS8EncodedKeySpec pkcs8 = new PKCS8EncodedKeySpec(IOUtils.toByteArray(stream));
RSAPrivateKey privKey = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8);
stream.close();
stream = null;

KeyStore ks = KeyStore.getInstance("JKS");
char[] ksPassword = "mypass".toCharArray();

ks.load(null, ksPassword);
ks.setKeyEntry(alias, privKey, ksPassword, chain);

KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
kmf.init(ks, ksPassword);

TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
tmf.init(ks);

sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

//  serverHttps.setHttpsConfigurator(new HttpsConfigurator(sslContext));
serverHttps.setHttpsConfigurator ( new HttpsConfigurator( sslContext )
{
    @Override
    public void configure ( HttpsParameters params )
    {
        try
        {
            // initialise the SSL context
            SSLContext c = SSLContext.getDefault ();
            SSLEngine engine = c.createSSLEngine ();
            params.setNeedClientAuth ( false );
            params.setCipherSuites ( engine.getEnabledCipherSuites () );
            params.setProtocols ( engine.getEnabledProtocols () );

            // get the default parameters
            SSLParameters defaultSSLParameters = c.getDefaultSSLParameters ();
            params.setSSLParameters ( defaultSSLParameters );
        }
        catch ( Exception ex )
        {
            System.out.println( "Failed to configure HTTPS server: "+ex.getMessage() );
            System.exit(100);
        }
    }
} );
