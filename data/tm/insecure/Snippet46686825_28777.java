KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmf.init(null, null);

X509KeyManager manager = (X509KeyManager) kmf.getKeyManagers()[0];
KeyManager km = new X509KeyManager() {
    @Override
    public String[] getClientAliases(String s, Principal[] principals) {
        return manager.getServerAliases(s, principals);
    }

    @Override
    public String chooseClientAlias(String[] strings, Principal[] principals, Socket socket) {
        return manager.chooseClientAlias(strings, principals, socket);
    }

    @Override
    public String[] getServerAliases(String s, Principal[] principals) {
        return manager.getServerAliases(s, principals);
    }

    @Override
    public String chooseServerAlias(String s, Principal[] principals, Socket socket) {
        return manager.chooseServerAlias(s, principals, socket);
    }

    @Override
    public X509Certificate[] getCertificateChain(String s) {
        // You can use `s` to select the appropriate file

        try {
            File file = new File("path to certificate");

            try(InputStream is = new FileInputStream(file)) {
                CertificateFactory factory = CertificateFactory.getInstance("X.509");
                return new X509Certificate[] {
                        (X509Certificate) factory.generateCertificate(is)
                };
            }
        }
        catch (CertificateException| IOException  e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public PrivateKey getPrivateKey(String s) {
        // You can use `s` to select the appropriate file

        // load and private key from selected certificate
        // this use for certificate authorisation

        try {
            File file = new File("private key file");
            byte buffer[] = Files.readAllBytes(file.toPath());

            KeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory factory = KeyFactory.getInstance("RSA");

            return factory.generatePrivate(keySpec);
        }
        catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;
    }
};

TrustManager tm = new X509TrustManager() {
    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        try {
            File file = new File("path to certificate");

            try(InputStream is = new FileInputStream(file)) {
                CertificateFactory factory = CertificateFactory.getInstance("X.509");
                return new X509Certificate[] {
                        (X509Certificate) factory.generateCertificate(is)
                };
            }
        }
        catch (CertificateException| IOException  e) {
            e.printStackTrace();
        }

        return null;
    }
};

TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init((KeyStore)null); //use java system trust certificates

TrustManager managers[] = new TrustManager[tmf.getTrustManagers().length + 1];
System.arraycopy(tmf.getTrustManagers(), 0, managers, 0, tmf.getTrustManagers().length);
managers[managers.length - 1] = tm;

SSLContext context = SSLContext.getInstance("TLS");
context.init(new KeyManager[]{ km }, managers, new SecureRandom());

URL url = new URL("https://............/");
HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
connection.setSSLSocketFactory(connection.getSSLSocketFactory());

connection.connect();
