public class CustomKeyManager extends X509ExtendedKeyManager {

    private final X509ExtendedKeyManager base;

    public CustomKeyManager(X509ExtendedKeyManager base) {
        this.base = base;
    }

    /* Lots of methods omitted */

    static SSLContext SSL_CONTEXT;

    static void updateSSL(String keyStoreFile, String keyStorePassword){
        System.setProperty("org.apache.axis.components.net.SecureSocketFactory", "com.spanlink.cfg.crypto.HostNameSecureSocketFactory");
        AxisProperties.setProperty("axis.socketSecureFactory", "com.spanlink.cfg.crypto.HostNameSecureSocketFactory");
        KeyStore keyStore = KeyStore.getInstance("jks");
        keyStore.load(new FileInputStream(keyStoreFile), keyStorePassword.toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, keyStorePassword.toCharArray());

        KeyManager[] oldManagers = kmf.getKeyManagers();
        KeyManager[] newManagers = new KeyManager[oldManagers.length];
        for (int i = 0; i < oldManagers.length; i++) {
            if (oldManagers[i] instanceof X509ExtendedKeyManager) {
                newManagers[i] = new CustomKeyManager((X509ExtendedKeyManager) oldManagers[i]);
            }else{
                newManagers[i] = oldManagers[i];
            }
        }

        SSL_CONTEXT = SSLContext.getInstance("SSL");
        SSL_CONTEXT.init(newManagers, null, null);
    }


}

public class CustomSecureSocketFactory extends JSSESocketFactory {

    public CustomSecureSocketFactory(Hashtable table) {
        super(table);
        super.sslFactory = CustomKeyManager.SSL_CONTEXT.getSocketFactory();
    }

}
