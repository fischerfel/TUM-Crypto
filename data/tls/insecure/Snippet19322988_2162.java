        SSLContext ctx = SSLContext.getInstance("SSLv3");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(Client.class.getResourceAsStream("hfcerts"), "changeit".toCharArray() );
        kmf.init(ks, "changeit".toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        TrustManager[] trustManagers = tmf.getTrustManagers();
        ctx.init(kmf.getKeyManagers(), trustManagers, new SecureRandom());

        SSLContext.setDefault(ctx);

        ((BindingProvider) port).getRequestContext().put(JAXWSProperties.SSL_SOCKET_FACTORY, ctx.getSocketFactory());
