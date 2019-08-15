public String decryptBlowfish(String to_decrypt, String strkey) {
    System.out.println(to_decrypt);
    try {
        SecretKeySpec key = new SecretKeySpec(strkey.getBytes(), "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypted = cipher.doFinal(to_decrypt.getBytes());
        return new String(decrypted);
    } catch (Exception e) {
        System.out.println(e.getMessage());
        ;
        return null;
    }
}
