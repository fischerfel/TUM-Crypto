final Cipher encryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "BC");
encryptCipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec("7c54367a45b37a192abc2cd7f4520304".getBytes(), "AES"));
final byte[] encrypt = encryptCipher.doFinal("This is my text".getBytes());
System.out.println(new String(Base64.encode(encrypt)));

final Cipher decryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "BC");
decryptCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec("7c54367a45b37a192abc2cd7f4520304".getBytes(), "AES"));
final byte[] decrypt = decryptCipher.doFinal(encrypt);
System.out.println(new String(decrypt));
