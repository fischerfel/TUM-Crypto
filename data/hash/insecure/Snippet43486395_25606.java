private static String MD5Encode(String sourceString) {
try {
byte[] bytesOfMessage = sourceString.getBytes("UTF-8");
MessageDigest md = MessageDigest.getInstance("MD5");
// byte array of md5 hash
byte[] md5 = md.digest(bytesOfMessage);
// we convert bytes to hex as php's md5() would do
StringBuffer stringBuffer = new StringBuffer();
for (int i = 0; i < md5.length; i++) {
stringBuffer.append(Integer.toString((md5[i] & 0xff) + 0x100, 
16).substring(1));
}
return stringBuffer.toString();
} catch (Exception e) {
}
return null;
}
