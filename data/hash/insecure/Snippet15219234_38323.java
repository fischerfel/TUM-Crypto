public static void main(String[] args) {

  try {
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update("password".getBytes("UTF-8"));
    byte[] digest = md.digest();
    System.out.println(DatatypeConverter.printHexBinary(digest));

  } catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
  } catch (UnsupportedEncodingException e) {   
    e.printStackTrace();
  }
}
