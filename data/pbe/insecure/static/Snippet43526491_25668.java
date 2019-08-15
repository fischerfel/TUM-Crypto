//Loading the OS default keystore
KeyStore ks = KeyStore.getInstance("KeychainStore", "Apple");
ks.load(null, null);

//Reading keys
Enumeration<String> aliases = ks.aliases();
while (aliases.hasMoreElements()) {
    String alias = aliases.nextElement();
    if (ks.isKeyEntry(alias)) {
        Key key = ks.getKey(alias, "anything".toCharArray()); //this invokes the OS popup
        if (key == null) {
            System.err.println(alias + " could not be read");
        } else {
            System.out.println(alias + " (algorithm = " + key.getAlgorithm() + ", format = " + key.getFormat() + ")");
        }
    }
}

//Writing keys (does not work yet)
String secretExample = "my very secret secret";
SecretKey generatedSecret = SecretKeyFactory.getInstance("PBE").generateSecret(new PBEKeySpec(secretExample.toCharArray()));

ks.setKeyEntry("my.app.Secret", generatedSecret, null, null); //throws java.security.KeyStoreException: Key protection algorithm not found: java.security.KeyStoreException: Key is not a PrivateKey
ks.setKeyEntry("my.app.Secret", secretExample.getBytes(), null); //throws java.security.KeyStoreException: key is not encoded as EncryptedPrivateKeyInfo
