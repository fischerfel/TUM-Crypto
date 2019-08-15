        KeyStore ksTrust = KeyStore.getInstance("PKCS12");
        ksTrust.load(context.getResources().openRawResource(R.raw.pkcsserver), passphrase);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        tmf.init(ksTrust);

        //get context
        SSLContext sslContext = SSLContext.getInstance("TLS");

        //init context
        sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
