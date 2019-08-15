System.setProperty("javax.net.ssl.trustStore", "U:\\Certificados\\efau.truestore");
System.setProperty("javax.net.ssl.trustStoreType", "jks");
System.setProperty("javax.net.ssl.trustStorePassword", "oiadad");

KeyManagerFactory kFac;
SSLContext sslContext;
SSLSocketFactory sockFactory = null;
KeyStore ks;

try {
    // load keystore present in windows and print aliases found (only one, so nextElement always prints same information (name of certificate inside usb token I want to open))
    ks = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
    ks.load(null, null);
    System.out.println(ks.aliases().nextElement());
    System.out.println(ks.aliases().nextElement());

    // try to load my certificate specifically from all certificates and passes necessary token password to it
    InputStream in = IOUtils.toInputStream(ks.aliases().nextElement(), "UTF-8");
    System.out.println(in);
    ks.load(in, password);

    // print certificate to check if I have it
    System.out.println(ks.getCertificate(ks.aliases().nextElement()));

    // get ssl context and key manager factory
    sslContext = SSLContext.getInstance("SSL", "SunJSSE");
    kFac = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kFac.init(ks,null);

    sslContext.init(kFac.getKeyManagers(), null, null);
    sockFactory = sslContext.getSocketFactory();

    // start connection with website
    HttpsURLConnection conn = (HttpsURLConnection)new URL(<my-https-url>).openConnection();
    conn.setRequestMethod("GET");
    conn.setDoInput(true);
    conn.setSSLSocketFactory(sockFactory);

    int responseCode = conn.getResponseCode();
    System.out.println("RESPONSE: " + responseCode);

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
