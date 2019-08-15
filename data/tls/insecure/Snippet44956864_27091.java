    if (server) {// load the keystore
        try (FileInputStream fis = new FileInputStream(ks)) {
            CONTEXT = SSLContext.getInstance("SSL");

            // load the keystore from the file
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(fis, PASSWORD);

            // setup the KeyManagerFactory (used by the server)
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keystore, PASSWORD);

            CONTEXT.init(kmf.getKeyManagers(), null, null);
        }
        catch (Exception e) {
        }
    }
    else {
        CONTEXT = SSLContext.getInstance("SSL");
        CONTEXT.init(null, new TrustManager[] { new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        } }, null);
    }
