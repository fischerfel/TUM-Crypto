public class KeyCreatorClass {

    KeyStore keyStore;
    KeyGenerator keyGenerator;
    Cipher cipher;

    public void createKey(String keyAlias) { //I call this method only once in the onCreate() method of another activity, with keyAlias "A"
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            keyStore.load(null);
            keyGenerator.init(
                    new KeyGenParameterSpec.Builder(keyAlias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setUserAuthenticationRequired(false)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .setRandomizedEncryptionRequired(false)
                        .build());
            keyGenerator.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String printCipherText(String keyAlias, String plainText){ //I call this method many times with the same keyAlias "A" and same plaintext in the same activity
        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(keyAlias, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return byteToHex(cipher.doFinal(plainText.getBytes()));
        }catch(Exception e){
            e.printStackTrace();
        }
        return "BUG";
    }

    private String byteToHex(byte[] byteArray){
        StringBuilder buf = new StringBuilder();
        for (byte b : byteArray)
            buf.append(String.format("%02X", b));
        String hexStr = buf.toString();
        return hexStr;
    }
} 
