    CertificateFactory cf = CertificateFactory.getInstance("X.509");
        ca = cf.generateCertificate(caInput);
        System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

         caInput = this.getResources().openRawResource(R.raw.i1);
         java.security.cert.Certificate ci1;
         ci1 = cf.generateCertificate(caInput);
         System.out.println("i1=" + ((X509Certificate) ci1).getSubjectDN());
         keyStore.setCertificateEntry("ci1", ci1);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        URI u = URI.create("https://myurl/services/myservice");

    SoapObject client = new SoapObject("", "dummy");
         SoapSerializationEnvelope envelope = new    SoapSerializationEnvelope(SoapEnvelope.VER11);
         envelope.bodyOut = client;

         HttpsTransportSE ht = new HttpsTransportSE(u.getHost(), u.getPort(), u.getPath(), 10000);
         ((HttpsServiceConnectionSE) ht.getServiceConnection()).setSSLSocketFactory(context.getSocketFactory());

         ht.call("", envelope);
