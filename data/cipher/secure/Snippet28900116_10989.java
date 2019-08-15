import java.security.(...);

import javax.crypto.Cipher;

String publickeybase64 = go_get_file_bytes_as_base64("public_key.der");
byte[]decode = Base64.decode(publickeybase64, Base64.DEFAULT);
CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
Certificate certificate = certificateFactory.generateCertificate(new ByteArrayInputStream(decode));
PublicKey publicKey = certificate.getPublicKey();
Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
cipher.init(Cipher.ENCRYPT_MODE, publicKey);
String plaintext = "hello world";
String encryptedstring = new String(Base64.encode(cipher.doFinal(plaintext.getBytes()),Base64.NO_WRAP));
