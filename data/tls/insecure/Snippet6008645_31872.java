        FileInputStream fisTrusted = null;

        String keyStoreType = "jks";

        String passphrase = "password";

        String passphraseTrusted = "password";

        KeyStore ks = KeyStore.getInstance(keyStoreType);

        fisjks = new FileInputStream("C:/CFC/Certs/client.jks");

        ks.load(fisjks, passphrase.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");

        kmf.init(ks, passphrase.toCharArray());

        KeyStore ks1 = KeyStore.getInstance(keyStoreType);

        fisTrusted = new FileInputStream("C:/CFC/Certs/clientTruststore.jks");

        ks1.load(fisTrusted, passphraseTrusted.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance("PKIX");

        tmf.init(ks1);

        SSLContext sslc = SSLContext.getInstance("SSLv3");

        sslc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        HttpsTransportInfo https = new HttpsTransportInfo();

        https.setKeyManagers(kmf.getKeyManagers());

        https.setTrustManagers(tmf.getTrustManagers());
