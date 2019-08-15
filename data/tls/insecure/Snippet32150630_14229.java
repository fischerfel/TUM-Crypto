                 CertificateFactory cf = CertificateFactory.getInstance("X.509");
             InputStream caInput = getResources().openRawResource(R.drawable.cert);
             Certificate ca;
             try {
                 ca = cf.generateCertificate(caInput);
                 System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
             } finally {
                 caInput.close();
             }
             String keyStoreType = KeyStore.getDefaultType();
             KeyStore keyStore = KeyStore.getInstance(keyStoreType);
             keyStore.load(null, null);
             keyStore.setCertificateEntry("ca", ca);
             String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
             TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
             tmf.init(keyStore);
             SSLContext context = SSLContext.getInstance("TLS");
             context.init(null, tmf.getTrustManagers(), null);
            mRequestQueue =Volley.newRequestQueue(getApplicationContext(), new HurlStack(null, context.getSocketFactory()));
