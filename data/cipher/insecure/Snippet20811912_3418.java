public static String encrypt(String content, String sKey) {
  try {
    SecretKey secretKey = null;
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
    secureRandom.setSeed(sKey.getBytes());
    kgen.init(128, secureRandom);
    secretKey = kgen.generateKey();
    byte[] enCodeFormat = secretKey.getEncoded();
    SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");

    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, key);

    byte[] byteContent = content.getBytes("utf-8");
    byte[] result = cipher.doFinal(byteContent);

    return ByteUtil.parseByte2HexStr(result);
  } catch (Exception e) {
    e.printStackTrace();
  }
  return content;
}


## ByteUtil.java
...
public static String parseByte2HexStr(byte buf[]) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < buf.length; i++) {
        String hex = Integer.toHexString(buf[i] & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        sb.append(hex.toUpperCase());
    }
    return sb.toString();
}
