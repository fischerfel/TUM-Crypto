public static byte[] encrypt(String x) throws Exception {
      java.security.MessageDigest digest = null;
      digest = java.security.MessageDigest.getInstance("SHA-1");

      digest.reset();

      digest.update(x.getBytes("UTF-8"));

      return digest.digest();
 }
