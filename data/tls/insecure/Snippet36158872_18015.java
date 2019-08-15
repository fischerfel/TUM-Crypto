private void Connect() throws NoSuchAlgorithmException, FileNotFoundException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException, KeyManagementException {
    SSLContext ctx = SSLContext.getInstance("TLS");
    KeyManager[] keyManagers;
    KeyStore keyStore = KeyStore.getInstance("pkcs12");
    FileInputStream keyStoreFile = new FileInputStream(new File("Certificate.pfx"));
    String keyStorePassword = "password";
    keyStore.load(keyStoreFile, keyStorePassword.toCharArray());
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(keyStore, keyStorePassword.toCharArray());
    keyManagers = kmf.getKeyManagers();
    ctx.init(keyManagers, null, new SecureRandom());

    SSLSocketFactory sslSocketFactory = ctx.getSocketFactory();
    URL url = new URL("https://example.com");
    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
    urlConnection.setSSLSocketFactory(sslSocketFactory);
    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
    String inputLine;
    while ((inputLine = in.readLine()) != null) {
        System.out.println(inputLine);
    }
    in.close();
}
