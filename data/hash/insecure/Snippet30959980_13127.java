public void test() {
    String sKSN = "ffff1234560006800010";
    String sBDK = "E08A46B616230152230DB9C8DF94C75E";
    byte[] dikKSN = new byte[10];
    byte[] KSN8 = new byte[8];
    byte[] BDK = new byte[16];
    byte[] lKey = new byte[8];
    byte[] rKey = new byte[8];
    String retKey = "";
    String lgTxt = "";
    dikKSN = this.fromHex(sKSN);  // convert hex to byte array
    BDK = this.fromHex(sBDK); // convert hex to byte array
    KSN8 = this.copyByte8(dikKSN); //use the first 8 values
    lKey = this.tDESEncrypt(KSN8, BDK);
}
private byte[] tDESEncrypt(byte[] plainTextBytes, byte[] kb) {
  byte[] cipherText = null;
  try {
    final MessageDigest md = MessageDigest.getInstance("md5");
    final byte[] digestOfPassword = md.digest(kb);
    final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
    for (int j = 0, k = 16; j < 8;) {
      keyBytes[k++] = keyBytes[j++];
    }
    final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
    final Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding"); 
    final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
    cipher.init(Cipher.ENCRYPT_MODE, key, iv);
    cipherText = cipher.doFinal(plainTextBytes);
  } catch (Exception ex) {
    ex.printStackTrace();
  }
  return cipherText;
}
