        // Create a key store and add private key and certificate that the client will use for authentication
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(null, null);
        keyStore.setKeyEntry("client", getPKFromFile("clientPrivateKey.pem"), "1234567".toCharArray(), new java.security.cert.Certificate[] { getX509CertificateFromFile("clientCertificate.pem"), getX509CertificateFromFile("intermediateCertificate.pem"), getX509CertificateFromFile("rootCertificate.pem")});

        // Create the trust manager
        final TrustManager[] trustCerts = new TrustManager[] { new X509TrustManager() {
            @Override
            public void checkClientTrusted( final X509Certificate[] chain, final String authType ) {
            }
            @Override
            public void checkServerTrusted( final X509Certificate[] chain, final String authType ) {
            }
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                try {
                    X509Certificate[] certs = new X509Certificate[1];
                    // add a certificate that has nothing to do with the server certificate
                    String DigiSignQualifiedPublicCA_FilePath = System.getProperty("DigiSignQualifiedPublicCA", "./sslstore/DigiSignQualifiedPublicCA.cer");
                    File DigiSignQualifiedPublicCA_File = new File(DigiSignQualifiedPublicCA_FilePath);
                    certs[0] = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new FileInputStream(DigiSignQualifiedPublicCA_File));

                    return certs;
                } catch (Exception ex) {
                    Logger.getLogger(Axis2ServerTest.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                }
            }
        } };

        SSLContext sslCtx = SSLContext.getInstance("TLSv1.2");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, "1234567".toCharArray());
        KeyManager[] keyManagers = kmf.getKeyManagers();
        sslCtx.init(keyManagers, trustCerts, null);

        HelloPojoStub stub = new HelloPojoStub("https://localhost/axis2/services/HelloPojo?wsdl");

        stub._getServiceClient().getOptions().setProperty(HTTPConstants.CUSTOM_PROTOCOL‌​_HANDLER, new Protocol("https",(ProtocolSocketFactory)new SSLProtocolSocketFactory(sslCtx),443));
