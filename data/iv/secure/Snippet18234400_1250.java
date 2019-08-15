public static String decrypt(String hexEncoded) throws Exception {
   byte[] decodedBytes = hexStringToBytes(hexEncoded);
   int ivIndex = decodedBytes.length - 16;
   cipher.init(Cipher.DECRYPT_MODE, encryptionKey,
       new IvParameterSpec(decodedBytes, ivIndex, 16));
   return reconstructedPlainText(cipher.doFinal(decodedBytes, 0, ivIndex));
}
