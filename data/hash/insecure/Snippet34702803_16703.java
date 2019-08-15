public static String MD5(String password) {
  String result = "";
    try {
      MessageDigest provider = MessageDigest.getInstance("MD5");

      byte[] hash = provider.digest(password.getBytes(StandardCharsets.UTF_8));
      MessageDigest m = MessageDigest.getInstance("MD5");
      byte[] digest = m.digest(hash);
      String s = new BigInteger(1, digest).toString(16).toLowerCase();

      result = s;
    } catch (Exception ex) {
        LogUtil.error_Logging("Authentication", ex.getMessage());
    }   
    return result;
}
