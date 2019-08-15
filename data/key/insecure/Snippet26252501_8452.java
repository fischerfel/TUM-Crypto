public static void main(String[] args) throws Exception {
    Security.addProvider(new BouncyCastleProvider());

    byte[] keyBytes = new byte[16];
    byte[] iv = new byte[16];
    SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
    javax.crypto.Cipher cipher = Cipher.getInstance("PBEWITHSHA256AND128BITAES-CBC-BC");
    cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
    final byte[] ciphertext = cipher.doFinal("The PBE encryption has gone!".getBytes(UTF_8));

    Cipher dCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    dCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
    final byte[] plain = dCipher.doFinal(ciphertext);

    System.out.println(new String(plain, UTF_8));
}
