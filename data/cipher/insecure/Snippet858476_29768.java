public void mytestSimple(long code, String password) throws Exception {
    SecretKey key = new SecretKeySpec(password.getBytes(), "DES");
    Cipher ecipher = Cipher.getInstance("DES");
    ecipher.init(Cipher.ENCRYPT_MODE, key);
    System.out.println(ecipher.getOutputSize(8));

    byte[] encrypted = ecipher.doFinal(numberToBytes(code));
    System.out.println(encrypted + "--" + encrypted.length);

    Cipher dcipher = Cipher.getInstance("DES");
    dcipher.init(Cipher.DECRYPT_MODE, key);
    byte[] decrypted = dcipher.doFinal(encrypted);
    System.out.println(bytesToNumber(decrypted) + "--" + decrypted.length);
}

public void testSimple() throws Exception {
    mytestSimple(981762654986L, "password");
}
