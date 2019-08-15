        KeyStore identityStore = KeyStore.getInstance(KeyStore.getDefaultType());
        ClassPathResource keystore = new ClassPathResource(cadaBackendCertFile);

        identityStore.load(keystore.getInputStream(), cadaBackendCertFilePassword.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(identityStore, cadaBackendCertFilePassword.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(identityStore);

        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(kmf.getKeyManagers(), new TrustManager[] {new X509TrustManager(){
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
            public void checkServerTrusted(X509Certificate[] chain,String authType) throws CertificateException {               
            }
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }       
        }}, new SecureRandom());

        SSLSocketFactory fac = ctx.getSocketFactory();
        Socket sslsock = fac.createSocket(cadaBackendEndpoint, cadaBackendPort);
        TTransport transport = new TSocket(sslsock);

        TProtocol proto = new TBinaryProtocol(transport);
        cadaBackendClient = new Client(proto);
