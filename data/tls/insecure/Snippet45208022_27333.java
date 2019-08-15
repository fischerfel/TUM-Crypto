CertificateFactory cf;
            try {
                cf = CertificateFactory.getInstance("X.509");

                AssetManager assManager = Utils.context.getAssets();
                InputStream is = null;
                is = assManager.open("certificate.cer");
                InputStream caInput = new BufferedInputStream(is);
                Certificate ca;
                try {
                    ca = cf.generateCertificate(caInput);

                    System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
                } finally {
                    caInput.close();
                }

                // Create a KeyStore containing our trusted CAs
                String keyStoreType = KeyStore.getDefaultType();
                KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                keyStore.load(null, null);
                keyStore.setCertificateEntry("ca", ca);

                // Create a TrustManager that trusts the CAs in our KeyStore
                String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                tmf.init(keyStore);

                // create CouchbaseLiteHttpClientFactory
               CouchbaseLiteHttpClientFactory cblHttpClientfactory = new CouchbaseLiteHttpClientFactory
                        (database.getPersistentCookieStore());

                // Create an SSLContext that uses our TrustManager
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, tmf.getTrustManagers(), null);
                cblHttpClientfactory.setSSLSocketFactory(context.getSocketFactory());
                manager.setDefaultHttpClientFactory(cblHttpClientfactory);

            } catch (CertificateException e) {
}
