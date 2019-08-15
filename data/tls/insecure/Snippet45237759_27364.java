public String getJSON(String store, Context context) throws IOException, LogoNotFoundException {
        try {
            // Load CAs from an InputStream
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = new BufferedInputStream(context.getResources().openRawResource(R.raw.abyx));
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

            // Create an SSLContext that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            store = URLEncoder.encode(store, "UTF-8");
            String response;
            URL url = new URL("https://www.abyx.be/loyalty/public/logo/" + URLEncoder.encode(store, "utf-8"));
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(sslContext.getSocketFactory());
            connection.connect();
            Certificate[] certificates = connection.getServerCertificates();
            for (Certificate cert: certificates) {
                System.out.println(cert);
            }
            connection.setRequestMethod("GET");
            int statusCode = connection.getResponseCode();
            if (statusCode == 200) {
                InputStream in = new BufferedInputStream(connection.getInputStream());
                response = IOUtils.toString(in, "UTF-8");
            } else if (statusCode == 404) {
                throw new LogoNotFoundException();
            } else {
                throw new IOException("Unable to connect to Loyalty API!");
            }
            return response;
        } catch (KeyStoreException | NoSuchAlgorithmException | KeyManagementException | CertificateException e) {
            throw new IOException(e);
        }
    }
