class TripleDES
{

  private final String key;

  public static void main(String... args)
    throws Exception
  {
    TripleDES td = new TripleDES("1032FD2CD64A9D7FA4D061F76B04BFEA");
    String decrypted = td.decrypt("AC9C5A46A63FC9EA");
    System.out.println("expecting: 04286EDDFDEA6BD7");
    System.out.println("found: " + decrypted);
  }

  TripleDES(String key)
  {
    this.key = key;
  }

  public String decrypt(String input)
    throws Exception
  {
    byte[] tmp = h2b(this.key);
    byte[] key = new byte[24];
    System.arraycopy(tmp, 0, key, 0, 16);
    System.arraycopy(tmp, 0, key, 16, 8);
    Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "DESede"));
    byte[] plaintext = cipher.doFinal(h2b(input));
    return b2h(plaintext);
  }

  private static byte[] h2b(String hex)
  {
    if ((hex.length() & 0x01) == 0x01)
      throw new IllegalArgumentException();
    byte[] bytes = new byte[hex.length() / 2];
    for (int idx = 0; idx < bytes.length; ++idx) {
      int hi = Character.digit((int) hex.charAt(idx * 2), 16);
      int lo = Character.digit((int) hex.charAt(idx * 2 + 1), 16);
      if ((hi < 0) || (lo < 0))
        throw new IllegalArgumentException();
      bytes[idx] = (byte) ((hi << 4) | lo);
    }
    return bytes;
  }

  private static String b2h(byte[] bytes)
  {
    char[] hex = new char[bytes.length * 2];
    for (int idx = 0; idx < bytes.length; ++idx) {
      int hi = (bytes[idx] & 0xF0) >>> 4;
      int lo = (bytes[idx] & 0x0F);
      hex[idx * 2] = (char) (hi < 10 ? '0' + hi : 'A' - 10 + hi);
      hex[idx * 2 + 1] = (char) (lo < 10 ? '0' + lo : 'A' - 10 + lo);
    }
    return new String(hex);
  }

}
