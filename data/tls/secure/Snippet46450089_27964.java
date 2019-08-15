        KeyManagerFactory kmf = .... //generating KeyManagerFactory
        if (kmf == null) {
            throw new UnexpectedInternalException("Initialisation of SSLContext failed!");
        }
        KeyManager[] keyManagers = kmf.getKeyManagers();
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(keyManagers, null, null);
        SSLSocketFactory f = sslContext.getSocketFactory();
