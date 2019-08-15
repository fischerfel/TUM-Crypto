MessageDigest sha224 = MessageDigest.getInstance("SHA-224");
sha224.update(key.getBytes());

byte[] digest = sha224.digest();
StringBuffer buffer = new StringBuffer();

for(int i = 0; i < digest.length; i++) {
 buffer.append(String.valueOf(Integer.toHexString(0xFF & digest[i])));
}

return buffer.toString();
