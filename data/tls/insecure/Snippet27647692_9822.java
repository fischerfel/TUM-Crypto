            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

            //load keystore stream
            byte[] keystoreData = readInputStream(getAssets().open("client.keystore"));

            //load keystore
            ByteArrayInputStream bais = new ByteArrayInputStream(keystoreData);
            keyStore.load(bais, KEYSTORE_PASSWORD.toCharArray());
            //load truststore
            bais = new ByteArrayInputStream(keystoreData);
            trustStore.load(bais, KEYSTORE_PASSWORD.toCharArray());
            //load trustmanager
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);
            //init keymanager
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, KEYSTORE_PASSWORD.toCharArray());
            //create ssl context
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);


HostnameVerifier HOSTNAME_VERIFIER = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            List<String> allowedHostnames = new ArrayList<String>();
            allowedHostnames.add("pinterest.com");
            allowedHostnames.add("192.168.1.43");
            allowedHostnames.add("10.0.2.2");
            return allowedHostnames.indexOf(hostname) != -1;
        }
    };

                    //open https connection
                    URL url = new URL("https://" + SERVER_URL + ":" + SERVER_PORT + "/api/v1/publication/getDescriptor/" + publicationId);
                    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                    urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());
                    urlConnection.setHostnameVerifier(HOSTNAME_VERIFIER);

                    //read server response
                    byte[] serverResult = readInputStream(urlConnection.getInputStream());
