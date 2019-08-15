public static String encryptBlowfish(String to_encrypt, String strkey) {
    try {
        SecretKeySpec key = new SecretKeySpec(strkey.getBytes(), "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return new String(cipher.doFinal(to_encrypt.getBytes()).toString()); // Added here .toString() because otherwise I get some hardcoded text
    } catch (Exception e) { return null; }
}
