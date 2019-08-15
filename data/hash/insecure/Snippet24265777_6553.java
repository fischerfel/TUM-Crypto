// A password hashing method.
public static String hashPassword(String in, String salt) {
  try {
    MessageDigest md = MessageDigest.getInstance("MD5"); // <-- Or, SHA-256
    md.update(salt.getBytes());        // <-- Prepend salt.
    md.update(in.getBytes());
    // md.update(salt.getBytes());     // <-- Or, append salt.

    byte[] out = md.digest();
    return bytesToHex(out);            // <-- Return the Hex Hash.
  } catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
  }
  return "";
}

private static String bytesToHex(byte[] byteArr) {  
  StringBuilder sb = new StringBuilder();   
  Formatter fmt = new Formatter(sb);  
  for (byte b : byteArr) {  
    fmt.format("%02x", b);  
  }  
  return sb.toString();
}  
