// wrap key data in Key/IV specs to pass to cipher
SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");
IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
// create the cipher with the algorithm you choose
// see javadoc for Cipher class for more info, e.g.
Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
