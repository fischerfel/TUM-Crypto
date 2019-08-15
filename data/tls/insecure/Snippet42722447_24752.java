 try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = mContext.getResources().openRawResource(R.raw.entrust_l1k);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca = " + ((X509Certificate) ca).getSubjectDN());
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

            // Create an SSLContext that uses our TrustManager
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(7000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();

            switch (responseCode) {
                case HttpsURLConnection.HTTP_OK:

                    InputStream in = urlConnection.getInputStream();

                    Scanner scanner = new Scanner(in);
                    scanner.useDelimiter("\\A");

                    boolean hasInput = scanner.hasNext();
                    if (hasInput) {
                        return scanner.next();
                    } else {
                        return null;
                    }


                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
