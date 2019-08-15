userName = URLDecoder.decode(userName, "ISO-8859-1");

Cipher objCipherTunkicloud = Cipher.getInstance("RSA/ECB/PKCS1Padding");

objCipherTunkicloud.init(Cipher.DECRYPT_MODE, loadPrivateKey("/keylecordonbleu/private.key", "RSA"));

byte[] arrDecryptedKeyBytes = objCipherTunkicloud.doFinal(userName.getBytes(StandardCharsets.ISO_8859_1));

String tkn = new String(arrDecryptedKeyBytes);
