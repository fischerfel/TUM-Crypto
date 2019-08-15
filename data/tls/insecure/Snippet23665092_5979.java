System.setProperty("javax.net.ssl.trustStore", "U:\\Certificados\\efau.truestore");
System.setProperty("javax.net.ssl.trustStoreType", "jks");
System.setProperty("javax.net.ssl.trustStorePassword", "oiadad");

KeyManagerFactory kFac;
SSLContext sslContext;
SSLSocketFactory sockFactory = null;

SunPKCS11 providerMSCAPI = new SunPKCS11("u:/Certificados/etpkcs11.cfg");
Provider a = providerMSCAPI;
Security.addProvider(a);

KeyStore ks;
try {
    ks = KeyStore.getInstance("PKCS11");

    ks.load(null, password);

    InputStream in = IOUtils.toInputStream(ks.aliases().nextElement(), "UTF-8");
    ks.load(in, password);


    sslContext = SSLContext.getInstance("SSL", "SunJSSE");
    kFac = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kFac.init(ks,null);

    sslContext.init(kFac.getKeyManagers(), null, null);
    sockFactory = sslContext.getSocketFactory();

    HttpsURLConnection conn = (HttpsURLConnection)new URL(/*<my-url>*/).openConnection();
    conn.setRequestMethod("GET");
    conn.setDoInput(true);
    conn.setSSLSocketFactory(sockFactory);

    int responseCode = conn.getResponseCode();

    InputStream inputstream = conn.getInputStream();
    InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
    BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

    String line = null;
    String htmlResponse = "";

    while ((line = bufferedreader.readLine()) != null) {
        htmlResponse += line + "\n";
    }

} catch (KeyStoreException e) {
    e.printStackTrace();
} catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
} catch (CertificateException e) {
    e.printStackTrace();
} catch (IOException e) {
    e.printStackTrace();
} catch (NoSuchProviderException e) {
    e.printStackTrace();
} catch (UnrecoverableKeyException e1) {
    e1.printStackTrace();
} catch (KeyManagementException e1) {
    e1.printStackTrace();
}
