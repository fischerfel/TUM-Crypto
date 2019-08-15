SSLContext  sslcontext = SSLContext.getInstance("TLS");
String  keyStore = System.getProperty("javax.net.ssl.keyStore");
String  keyStoreType = System.getProperty("javax.net.ssl.keyStoreType", KeyStore.getDefaultType());
String  keyStorePassword = System.getProperty("javax.net.ssl.keyStorePassword","");
KeyManager[]    kms = null;
if (keyStore != null)
{
    KeyManagerFactory   kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    KeyStore    ks = KeyStore.getInstance(keyStoreType);
    if (keyStore != null && !keyStore.equals("NONE")) {
        fs = new FileInputStream(keyStore);
    ks.load(fs, keyStorePassword.toCharArray());
    if (fs != null)
        fs.close();
    char[]  password = null;
    if (keyStorePassword.length() > 0)
        password = keyStorePassword.toCharArray();
    kmf.init(ks,password);
    kms = kmf.getKeyManagers();
}
sslcontext.init(kms,null,null);
