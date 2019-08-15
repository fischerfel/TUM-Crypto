    SSLContext context = null;
    KeyManagerFactory kmf = null;
    KeyStore ks = null;
    char[] storepass = "somestringhere".toCharArray();
    char[] keypass = "somestringhere".toCharArray();

    try {
        context = SSLContext.getInstance("SSL");
    } catch (NoSuchAlgorithmException e3) {
        // TODO Auto-generated catch block
        e3.printStackTrace();
    }
    try {
        kmf = KeyManagerFactory.getInstance("SunX509");
    } catch (NoSuchAlgorithmException e2) {
        // TODO Auto-generated catch block
        e2.printStackTrace();
    }
    FileInputStream fin = null;
    try {
        fin = new FileInputStream("file here");
    } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    try {
        ks = KeyStore.getInstance("JKS");
    } catch (KeyStoreException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    try {
        ks.load(fin, storepass);
    } catch (NoSuchAlgorithmException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    } catch (CertificateException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }

    try {
        kmf.init(ks, keypass);
    } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    try {
        context.init(kmf.getKeyManagers(), null, null);
    } catch (KeyManagementException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    Client client = ClientBuilder.newBuilder().sslContext(context).build();

    WebTarget target = client
            .target("https://....");

    Builder builder = target.request();
