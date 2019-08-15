public static void main(String[] args) throws Exception {

    Cipher aes = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    SecretKeySpec key = new SecretKeySpec("YELLOW SUBMARINE".getBytes(), "AES");
    IvParameterSpec iv = new IvParameterSpec(new byte[16]);

    aes.init(Cipher.DECRYPT_MODE, key, iv);

    byte[] cipherText = DatatypeConverter.parseHexBinary("60FA36707E45F499DBA0F25B922301A57192FEBE51B66D25BBFCC348138FD3F7");

    System.out.println(DatatypeConverter.printHexBinary(aes.doFinal(cipherText)));
    System.out.println(DatatypeConverter.printHexBinary(aes.doFinal(cipherText)));
}
