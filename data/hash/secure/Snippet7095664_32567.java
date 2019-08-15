  private static final byte[] SALT = "NJui8*&N823bVvy03^4N".getBytes();

  public static final String getSHA256Hash(String secret)
  {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      digest.update(secret.getBytes());
      byte[] hash = digest.digest(SALT);
      StringBuffer hexString = new StringBuffer();
      for (int i = 0; i < hash.length; i++) {
        hexString.append(Integer.toHexString(0xFF & hash[i]));
      }
      return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } 
    throw new RuntimeException("SHA-256 realization algorithm not found in JDK!");
  }
