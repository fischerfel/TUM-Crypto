public static SSLContext getSSL()
{       
    try
    {
        // Load CAs from an InputStream
        // (could be from a resource or ByteArrayInputStream or ...)
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        // From https://www.washington.edu/itconnect/security/ca/load-der.crt
        AssetManager assetManager = BankNizwaApp.getAppContext().getAssets();
        AssetFileDescriptor assetFileDescriptor = assetManager.openFd("cert.der");  
        FileDescriptor fileDescriptor = assetFileDescriptor.getFileDescriptor();  
        FileInputStream stream = new FileInputStream(fileDescriptor);
        InputStream caInput = new BufferedInputStream(stream);
        Certificate ca = null;
        try {
            ca = (Certificate) cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } catch(Exception er)
        {
            System.out.println("ssdada "+er.getMessage());
        }finally {
            caInput.close();
        }
        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", (java.security.cert.Certificate) ca);
        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);
        // Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);           
        return context;
    }
    catch(Exception e1)
    {
        System.out.println("ssdada "+e1.getMessage());
        return null;
    }
}
