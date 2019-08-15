URL url = new URL("https://.........");

KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
KeyStore keyStore = KeyStore.getInstance("Windows-MY");
keyStore.load(null, null);
keyManagerFactory.init(keyStore);

/* You must also set your trust store */
KeyStore ts = KeyStore.getInstance("Windows-ROOT");
TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(ts);

/* Here you can implement a way to set your key alias 
** You can run through all key entries and implement a way
** to prompt the user to choose one - for simplicity I just set a
** name*/
String alias = "user1_alias";

/* Get your current KeyManager from the factory */
final X509KeyManager okm = (X509KeyManager)keyManagerFactory.getKeyManagers()[0];

/* Implement the Interface X509KeyManager */
X509KeyManager km = new X509KeyManager() {
    public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
         /* Implement your own logic to choose the alias
            according to the validity if the case, 
            or use the entry id or any other way, you can get 
            those values outside this class*/
         return alias;
    }

    public X509Certificate[] getCertificateChain(String alias) {
         return okm.getCertificateChain(alias);
    }
   /* Implement the other methods of the interface using the okm object */
};
SSLContext context = SSLContext.getInstance("TLS");
/* set the keymanager in the SSLContext */
context.init(new KeyManager[]{km}, tmf.getTrustManagers(), new SecureRandom());
SSLSocketFactory socketFactory = context.getSocketFactory();

HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
conn.setSSLSocketFactory(socketFactory);
