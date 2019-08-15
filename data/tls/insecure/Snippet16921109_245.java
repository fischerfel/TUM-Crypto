        String certPath = System.getProperty("my.cert.path");
        String certPassword = System.getProperty("my.cert.password");

        in = new FileInputStream(new File(certPath));
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(in, certPassword.toCharArray());
        in.close();

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, certPassword.toCharArray());
        KeyManager[] keymanagers = kmf.getKeyManagers();

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(keymanagers, null, null);
        SSLSocketFactory sslSocketFactory = context.getSocketFactory();

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        // do HTTP connection OPS :)
