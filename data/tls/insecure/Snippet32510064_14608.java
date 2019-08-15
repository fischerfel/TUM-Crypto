        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        AssetManager assetManager = MyApp.getAppContext().getAssets();
        InputStream caInput = assetManager.open("cert.pem");
        Certificate ca = null;
        try {
            ca = (Certificate) cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } catch(Exception er)
        {
            System.out.println("ssdad222a "+er.getMessage());
        }finally {
            caInput.close();
        }
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", (java.security.cert.Certificate) ca);
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);       
        return context;
