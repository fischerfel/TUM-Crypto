byte[] pk = getKeyFromServer(); // 191 bytes 
String keyString = new String(pk);
byte[] decoded = decode(keyString); // 139 bytes

try {
    keySpec = new X509EncodedKeySpec(decoded);
} catch (Exception e) {  e.printStackTrace();}

try {
    keyFactory = KeyFactory.getInstance("RSA", "IBMJCE");
} catch (Exception e) {  e.printStackTrace();}

try {
    publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
} catch (Exception e) {  e.printStackTrace();}

try {
    cipher = Cipher.getInstance("RSA/SSL/PKCS1Padding", "IBMJCE");
} catch (Exception e) { e.printStackTrace(); }

try {
   cipher.init(Cipher.ENCRYPT_MODE, publicKey);
} catch (Exception e) { e.printStackTrace(); }

try {   sendEncrypted(cipher.doFinal(pwd.getBytes()));
} catch (Exception e) { e.printStackTrace(); }
