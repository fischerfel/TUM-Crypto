String pwd = "usergivenpassword";
try {
     MessageDigest md = MessageDigest.getInstance("SHA-256");
     md.update(pwd.getBytes("ASCII"));
     byte[] sha1hash = md.digest();

     StringBuilder buf = new StringBuilder();
     for (byte b : sha1hash) {
          int halfbyte = (b >>> 4) & 0x0F;
          int two_halfs = 0;
          do {
              buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
              halfbyte = b & 0x0F;
          } while (two_halfs++ < 1);
      }

      String hashedPassword = buf.toString();

      Log.i("PWD", "-------------- hashedPassword: " + hashedPassword);

} catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
} catch (UnsupportedEncodingException e) {
    e.printStackTrace();
}
