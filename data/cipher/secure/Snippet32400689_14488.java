KeyStore.Entry entry;

//Get Android KeyStore
ks = KeyStore.getInstance(KeystoreHelper.KEYSTORE_PROVIDER_ANDROID_KEYSTORE);

// Weird artifact of Java API.  If you don't have an InputStream to load, you still need to call "load", or it'll crash.
ks.load(null);

// Load the key pair from the Android Key Store
entry = ks.getEntry(mAlias, null);

KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) entry;

//ERROR OCCURS HERE::
RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKeyEntry.getPrivateKey();

Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");

output.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
