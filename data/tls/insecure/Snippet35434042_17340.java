public static SSLContext getSSLContext() throws Exception {
        if (sslContext==null) {
            // loading CA from an InputStream
            InputStream is = AVApplication.getContext().getResources().openRawResource(R.raw.wildcard);
            String certificates = Converter.convertStreamToString(is);
            String certificateArray[] = certificates.split("-----BEGIN CERTIFICATE-----");

            // creating a KeyStore containing our trusted CAs
            KeyStore ks = KeyStore.getInstance("BKS");
            ks.load(null, null);
            for (int i = 1; i < certificateArray.length; i++) {
                certificateArray[i] = "-----BEGIN CERTIFICATE-----" + certificateArray[i];
                //LogAV.d("cert:" + certificateArray[i]);

                // generate input stream for certificate factory
                InputStream stream = IOUtils.toInputStream(certificateArray[i]);

                // CertificateFactory
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                // certificate
                Certificate ca;
                try {
                    ca = cf.generateCertificate(stream);
                } finally {
                    is.close();
                }

                ks.setCertificateEntry("av-ca" + i, ca);
            }
            // TrustManagerFactory
            String algorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
            // Create a TrustManager that trusts the CAs in our KeyStore
            tmf.init(ks);

            // Create a SSLContext with the certificate that uses tmf (TrustManager)
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
        }

        return sslContext;
    }
