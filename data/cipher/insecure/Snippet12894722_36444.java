public String saltTxt = "12345678";
public String Encrypt(String str) {
    try {
        KeySpec myKey = new DESKeySpec(saltTxt.getBytes("UTF8"));
        SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(myKey);
        Cipher ecipher = Cipher.getInstance("DES");
        ecipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] data = str.getBytes("UTF8");

        byte[] crypt = ecipher.doFinal(data);

        return new BASE64Encoder().encode(crypt);
    } catch (Exception ex) {
    }

    return null;
} 
