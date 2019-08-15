String algorithm = Security.getProperty("ssl.KeyManagerFactory.algorithm");
    if (null == algorithm) {
        algorithm = "SunX509";
    }
    try {
        KeyStore ks = KeyStore.getInstance("JKS");
        String file = FileLocator.getPath(this.keystorePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        ks.load(fileInputStream, this.keystorePassword.toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
        kmf.init(ks, this.keystorePassword.toCharArray());
        SSLContext serverContext = SSLContext.getInstance("TLS");
        serverContext.init(kmf.getKeyManagers(), null, null);
        this.sslEngine = serverContext.createSSLEngine();
        this.sslEngine.setUseClientMode(false);
    } catch (Exception e) {
        throw new Error("Failed to initialize the server-side SSLContext", e);
    }
