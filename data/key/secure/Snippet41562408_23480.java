public static byte[] decrypt(byte[] key, byte[] initVector, byte[] encryptedValue) {

    try {

        IvParameterSpec iv = new IvParameterSpec(initVector);
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/NOPADDING");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

        byte[] original = cipher.doFinal(encryptedValue);

        return original;
    } catch (Exception ex) {
        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
    }

    return null;
}
...
byte[] encpryted = Base64.getDecoder().decode(rd.readLine());
byte[] iv = Base64.getDecoder().decode(rd.readLine());
byte[] key = Base64.getDecoder().decode(rd.readLine());

byte[] output = decrypt(key, iv, encpryted);
