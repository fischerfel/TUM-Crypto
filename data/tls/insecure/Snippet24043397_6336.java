    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
    //Create an empty keystore that we can load certificate into
    trustStore.load(null);
    InputStream fis = new FileInputStream("cert_chain.crt");
    BufferedInputStream bis = new BufferedInputStream(fis);

    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    while(bis.available()>0) {
        Collection<? extends Certificate> certs = cf.generateCertificates(bis);
        Iterator<? extends Certificate> iter = certs.iterator();
        //Add each cert in the chain one at a time
        for(int i=0; i<certs.size(); i++) {
            Certificate cert = iter.next();
            String alias = "chaincert"+((i>0)?i:"");
            trustStore.setCertificateEntry(alias, cert);
        }
    }
    bis.close();
    fis.close();
//Add custom keystore to TrustManager
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(trustStore);
    SSLContext ctx = SSLContext.getInstance("TLSv1");
    ctx.init(null, tmf.getTrustManagers(), null);
