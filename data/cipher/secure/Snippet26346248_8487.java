public final String modulusString ="..............";
public final String publicExponentString = "AQAB";

/* Encryption */
byte[] modulebytes = Base64.decode(modulusString);
byte[] exponentbytes = Base64.decode(publicExponentString);
BigInteger module = new BigInteger(1,modulebytes);
BigInteger publicexponent = new BigInteger(1,exponentbytes);
RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(module, publicexponent);
KeyFactory fact = KeyFactory.getInstance("RSA");
PublicKey pubKey = fact.generatePublic(rsaPubKey);

Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.ENCRYPT_MODE, pubKey);

byte[] plainBytes = EncryptionValue.getBytes("UTF-8");
byte[] cipherData = cipher.doFinal( plainBytes );
String encryptedString = Base64.encode(cipherData);

return encryptedString;

/* Decryption */
byte[] modulebytes = Base64.decode(modulusString);
byte[] exponentbytes = Base64.decode(publicExponentString);

BigInteger modulus = new BigInteger(1, modulebytes );
BigInteger exponent = new BigInteger(1, exponentbytes);

RSAPrivateKeySpec rsaPrivKey = new RSAPrivateKeySpec(modulus, exponent);
KeyFactory fact = KeyFactory.getInstance("RSA");
PrivateKey privKey = fact.generatePrivate(rsaPrivKey);

Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.DECRYPT_MODE, privKey);

byte[] base64String = Base64.decode(DecryptionValue);
byte[] plainBytes = new String(base64String).getBytes("UTF-8");
plainBytes = cipher.update(plainBytes);
byte[] values = cipher.doFinal(plainBytes);

return new String(values, "UTF-8");
