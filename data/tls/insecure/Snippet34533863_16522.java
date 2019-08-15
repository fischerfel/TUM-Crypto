                ByteArrayInputStream derInputStream = new ByteArrayInputStream(app.certificateString.getBytes());
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509","BC");
                X509Certificate cert = (X509Certificate) certificateFactory.generateCertificate(derInputStream);
                String alias = "alias";//cert.getSubjectX500Principal().getName();

                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null);
                trustStore.setCertificateEntry(alias, cert);
                KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
                kmf.init(trustStore, null);
                KeyManager[] keyManagers = kmf.getKeyManagers();

                TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
                tmf.init(trustStore);
                TrustManager[] trustManagers = tmf.getTrustManagers();

                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(keyManagers, trustManagers, null);
                URL url = new URL(someURL);
                conn = (HttpsURLConnection) url.openConnection();
                conn.setSSLSocketFactory(sslContext.getSocketFactory());
