 static final String ENABLED_CIPHERS[] = {
    "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA",
    "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA",
    "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA",
    "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA",
    "TLS_DHE_RSA_WITH_AES_128_CBC_SHA",
    "TLS_DHE_RSA_WITH_AES_256_CBC_SHA",
    "TLS_DHE_DSS_WITH_AES_128_CBC_SHA",
    "TLS_ECDHE_RSA_WITH_RC4_128_SHA",
    "TLS_ECDHE_ECDSA_WITH_RC4_128_SHA",
    "TLS_RSA_WITH_AES_256_CBC_SHA",
    "TLS_RSA_WITH_AES_128_CBC_SHA",
    "SSL_RSA_WITH_3DES_EDE_CBC_SHA",
    "SSL_RSA_WITH_RC4_128_SHA",
    "SSL_RSA_WITH_RC4_128_MD5",
};
// put this in a place where it can be reused
static final String ENABLED_PROTOCOLS[] = {
        "TLSv1.2", "TLSv1.1", "TLSv1"
    };

   private void sslCon()
    {
        try {
                             // setup truststore to provide trust for the server certificate
              // load truststore certificate
            InputStream trustStoresIs = getResources().openRawResource(R.raw.client_ca);
            String trustStoreType = KeyStore.getDefaultType();
            KeyStore trustStore = KeyStore.getInstance(trustStoreType);
            trustStore.load(trustStoresIs, "spsoft_123".toCharArray());
            //keyStore.setCertificateEntry("ca", ca);

            // initialize trust manager factory with the read truststore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(trustStore);

            // setup client certificate
            // load client certificate
            InputStream keyStoreStream = getResources().openRawResource(R.raw.client_cert_key);
            KeyStore keyStore = null;
            keyStore = KeyStore.getInstance("BKS");
            keyStore.load(keyStoreStream, "your password".toCharArray());

            KeyManagerFactory keyManagerFactory = null;
            keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "your password".toCharArray());

            // Create an SSLContext that uses our TrustManager
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(keyManagerFactory.getKeyManagers(), tmf.getTrustManagers(), null);

            SSLSocketFactory sslsocketfactory = (SSLSocketFactory)context.getSocketFactory();
            InetAddress serverAddr = InetAddress.getByName("192.168.11.104");
            sslSocket = (SSLSocket) sslsocketfactory.createSocket(serverAddr, 5212);
            //String[] ciphers = sslSocket.getEnabledCipherSuites();
            sslSocket.setEnabledCipherSuites(ENABLED_CIPHERS);
            // put this right before setEnabledCipherSuites()!
            //sslSocket.setEnabledProtocols(ENABLED_PROTOCOLS);
            //InputStream inputStream =  sslSocket.getInputStream();
            OutputStream out = sslSocket.getOutputStream();

            Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
            sslSocket.close();


        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CertificateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
