KeyGenerator keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder("demo-alias", KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT);
builder.setKeySize(256);
builder.setBlockModes(KeyProperties.BLOCK_MODE_CBC);
builder.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
keyGenerator.init(builder.build());

// this key will work with a CipherObject ...
SecretKey key = keyGenerator.generateKey();

// Load the key from the Keystore
KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
keyStore.load(null);

// This key will not work with the Cipher Object
SecretKey notWorkingKey = (SecretKey) keyStore.getKey("demo-alias", null);

Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
// That call fails
cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(new byte[]{87, 99, -94, 23, -17, 26, 84, -117, 59, -59, 25, -88, -66, 86, -42, 78}));

byte[] crypted = cipher.doFinal("testdata".getBytes());
