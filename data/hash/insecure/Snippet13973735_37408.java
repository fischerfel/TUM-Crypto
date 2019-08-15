public static String getDigest(byte[] password) {

  return new String(Hex.encodeHex(new MessageDigest.getInstance("SHA").digest(password)));

}
