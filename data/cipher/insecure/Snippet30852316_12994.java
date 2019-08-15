String pinstr = new String();
pinstr = "5555";
try {
    EncryptDecrypt encryptor = new EncryptDecrypt(pinstr);
    //encryptor.encrypt(code);
    String encrypted = new String();
    encrypted = encryptor.encrypt(code);

    String decrypted = new String();
    decrypted = encryptor.decrypt(encrypted);

    Toast.makeText(MainActivity.this, decrypted, Toast.LENGTH_SHORT).show();
} catch (InvalidKeyException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
} catch (NoSuchAlgorithmException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
} catch (UnsupportedEncodingException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
} catch (NoSuchPaddingException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
} catch (IllegalBlockSizeException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
} catch (BadPaddingException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
}
public class EncryptDecrypt {
    private SecretKeySpec skeySpec;
    private Cipher cipher, cipher2;


    EncryptDecrypt(String password) throws NoSuchAlgorithmException,
    UnsupportedEncodingException, NoSuchPaddingException,
    IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        byte[] key = Arrays.copyOf(sha.digest(("ThisisMySalt1234" + password).getBytes("UTF-8")),
        16);
        skeySpec = new SecretKeySpec(key, "AES");
        cipher = Cipher.getInstance("AES");
        cipher2 = Cipher.getInstance("AES");
    }

    String encrypt(String clear) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        String encrypted = new String();

        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        byte[] encryptedBytes = null;
        encryptedBytes = cipher.doFinal(clear.getBytes());

        encrypted = Base64.encodeToString(encryptedBytes, Base64.DEFAULT);

        return encrypted;
    }

    // fehlerhaft
    String decrypt(String encryptedBase64) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String decrypted = new String();
        cipher2.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decodedBytes = null;


        decodedBytes = Base64.decode(encryptedBase64, Base64.DEFAULT);

        decrypted = cipher2.doFinal(decodedBytes).toString();

        return decrypted;

    }
}
