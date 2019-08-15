public void loadKeyStoreAndDoTrustToCertificates(String ksFile, String ksPass, String tsAlgo) throws Exception {
        FileInputStream ksCert = new FileInputStream(ksFile);

        KeyStore ks = KeyStore.getInstance(keyStoreType);
        ks.load(ksCert, ksPass.toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(tsAlgo);
        kmf.init(ks, ksPass.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tsAlgo);
        tmf.init(ks);

        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(kmf.getKeyManagers(), null, new SecureRandom());

        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
                    System.out.println("Ошибка: URL хост '" + urlHostName + "' отличается SSL хоста'" + session.getPeerHost() + "'.");
                }
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }
