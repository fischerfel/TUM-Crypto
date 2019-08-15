MessageDigest md = MessageDigest.getInstance("MD5");
byte[] b = md.digest(input.getBytes());
StringBuffer output = new StringBuffer();
for (int i = 0; i < b.length; i++) {
  String tmpStr = "0" + Integer.toHexString((0xff & b[i]));
  output.append(tmpStr.substring(tmpStr.length() - 2));
}
return output.toString();
