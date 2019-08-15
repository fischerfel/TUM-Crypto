 public static void main(String args[]) throws NoSuchAlgorithmException {
 String password="abcdef"; 
 System.out.println("{SHA-1}" + new sun.misc.BASE64Encoder().encode(java.security.MessageDigest.getInstance("SHA1").digest(password.getBytes())));
 }
