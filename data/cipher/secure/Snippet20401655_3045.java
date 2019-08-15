import java.security.*;

Cipher rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
rsa.init(Cipher.ENCRYPT_MODE, publicKey);
byte[] ciphertext = rsa.doFinal("my cleartext".getBytes());

rsa.init(Cipher.DECRYPT_MODE, privateKey);
String cleartext = new String(rsa.doFinal(ciphertext));
