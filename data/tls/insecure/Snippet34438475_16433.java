                KeyStore keyStore = ...;
                String algorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory tmf =TrustManagerFactory.getInstance(algorithm);
                tmf.init(keyStore);

                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, tmf.getTrustManagers(), null);

                // Open a HTTP  connection to  the URL
                conn = (HttpsURLConnection) url.openConnection();
                conn.setSSLSocketFactory(context.getSocketFactory());
