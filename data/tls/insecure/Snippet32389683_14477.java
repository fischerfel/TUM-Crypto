public class SSLScoketFactoryManage {

    public static SSLSocketFactory getSSLSocketFactory(String keyStoreName, String password) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException, IOException {
        KeyStore ks= getKeyStore(keyStoreName, password);
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(ks, password.toCharArray());

          SSLContext context = SSLContext.getInstance("TLS");
          context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());

          return context.getSocketFactory();
    }


    }
