// registration
if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null)
{
  Security.addProvider(new BouncyCastleProvider());
}

SecretKey secretKey = new SecretKeySpec("_mykey__mykey__mykey__mykey__myk".getBytes(), "AES");
IvParameterSpec iv = new IvParameterSpec("_iv__iv__iv__iv_".getBytes());
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

System.out.println(cipher.getProvider()); // prints "BC version 1.53"

// encryption
cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
cipher.update("clearContent".getBytes());
byte[] cipheredContent = cipher.doFinal();

// decryption
cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
cipher.update(cipheredContent);
byte[] clearContent = cipher.doFinal();

System.out.println(new String(clearContent)); // prints "clearContent"
