  public byte[] SHA512(String text) {
    byte[] sha1hash = new byte[50];
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-512");
      md.update(text.getBytes("UTF-8"), 0, text.length());
      sha1hash = md.digest();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return sha1hash;
  }

 --snip--
  hash = new String(SHA512(salt + password));
  System.out.println(hash);
