public static String encrypt(String plainText) throws Exception {
   encryptionKey = new SecretKeySpec(eKey.getBytes("UTF-8"), "AES");

   cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
   cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, initialisationVector);

   byte[] eDataAndIv = appendIvToEncryptedData(cipher.doFinal(plainText.getBytes("UTF-8")), initialisationVector.getIV());
   return bytesToHexString(eDataAndIv);
}

public static String decrypt(String hexEncoded) throws Exception {
   byte[] decodedBytes = hexStringToBytes(hexEncoded);

   ArrayList<byte[]> al = retreiveIvFromByteArray(decodedBytes);
   byte[] eData = al.get(0);
   byte[] iv = al.get(1);

   cipher.init(Cipher.DECRYPT_MODE, encryptionKey, new IvParameterSpec(iv));
   return reconstructedPlainText(cipher.doFinal(eData));
}

private static byte[] appendIvToEncryptedData(byte[] eData, byte[] iv) throws Exception {
   ByteArrayOutputStream os = new ByteArrayOutputStream();
   os.write(eData);
   os.write(iv);
   return os.toByteArray();
}

private static ArrayList<byte[]> retreiveIvFromByteArray(byte[] dataPlusIv) {
   ByteArrayOutputStream iv = new ByteArrayOutputStream(16);
   ByteArrayOutputStream eData = new ByteArrayOutputStream();

   iv.write(dataPlusIv, dataPlusIv.length - 16, 16);
   eData.write(dataPlusIv, 0, dataPlusIv.length - 16);

   ArrayList<byte[]> al = new ArrayList<byte[]>();
   al.add(eData.toByteArray());
   al.add(iv.toByteArray());

   return al;
}
