Strign text="text input"
java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
byte[] hash = digest.digest(text.getBytes("UTF-8"));
String uniqueValue=bin2hex(hash); //To Convert binary to hexadecimal string 


static String bin2hex(byte[] data) {
  return String.format("%0" + (data.length * 2) + 'x', new BigInteger(1, data));
}
