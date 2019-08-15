MessageDigest sha224 = MessageDigest.getInstance("SHA-224");
sha224.update(key.getBytes());

byte[] digest = sha224.digest();
StringBuffer buffer = new StringBuffer();

for(int i = 0; i < digest.length; i++) {
  String hex_string = Integer.toHexString(0xFF & digest[i]);
  if(hex_string.length()==1) hex_string = "0"+hex_string;
  buffer.append(hex_string);
}

return buffer.toString();
