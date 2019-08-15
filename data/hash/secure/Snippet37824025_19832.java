    MessageDigest digest = null;
    try {
      digest = MessageDigest.getInstance("SHA512");
      String password = "password";
      byte[] output = digest.digest( password.getBytes("UTF-8"));
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
