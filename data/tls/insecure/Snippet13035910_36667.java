try {
    StringBuffer trustStore = new StringBuffer("c:/Temp/certs/TrustStore");
            StringBuffer keyStore =  new StringBuffer("c:/Temp/certs/keystore.arun");
    StringBuffer keyStorePass = new StringBuffer("xxxxx");
               StringBuffer keyAlias = new StringBuffer("user");
        StringBuffer keyPass =  new StringBuffer("XXXX");

            TrustManagerFactory tmf =TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

    FileInputStream fis = new FileInputStream(trustStore.toString());
    KeyStore ks1 = KeyStore.getInstance("jks");
    ks1.load(fis, trustStorePass.toString().toCharArray());
            fis.close();
    tmf.init(ks1);
    TrustManager[] tms = tmf.getTrustManagers();
    FileInputStream fin = new FileInputStream(keyStore.toString());
    KeyStore ks2 = KeyStore.getInstance("jks");
    ks2.load(fin, keyStorePass.toString().toCharArray());
    fin.close();
    KeyManagerFactory kmf =
        KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(ks2, keyStorePass.toString().toCharArray());
    KeyManager[] kms = kmf.getKeyManagers();
    if (keyAlias != null && keyAlias.length() > 0) {
            for (int i = 0; i < kms.length; i++) {
                // We can only deal with instances of X509KeyManager
                if (kms[i] instanceof X509KeyManager)
                    kms[i] = new CustomKeyManager(
                            (X509KeyManager) kms[i], keyAlias.toString());
            }
        }

SSLContext context = SSLContext.getInstance("TLS");
    context.init(kms,tms, null);
    ssf = context.getSocketFactory();
 } catch (Exception e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
}
}

  public static SocketFactory getDefault() {

    return new CustomSSLSocketFactory();
}
