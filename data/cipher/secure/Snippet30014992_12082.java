javax.crypto.Cipher writer = Cipher.getInstance("AES/CBC/PKCS5Padding");

String keyOf24Chars = "abcdefghijklmnopqrstuvwx";

IvParameterSpec ivSpec = getIv();

MessageDigest md = MessageDigest.getInstance("SHA-256");
md.reset();
byte[] keyBytes = md.digest(keyOf24Chars.getBytes("UTF-8"));

SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES/CBC/PKCS5Padding");

// secretKey.getAlgorithm(): "AES/CBC/PKCS5Padding"
// secretKey.getFormat(): "RAW"
// secretKey.getEncoded().length: 32

writer.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec); // java.security.InvalidKeyException: Illegal key size
