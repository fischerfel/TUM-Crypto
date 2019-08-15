userName = URLDecoder.decode(userName, "ISO-8859-1");
Cipher objCipherTunkicloud = Cipher.getInstance("RSA/ECB/PKCS1Padding");

objCipherTunkicloud.init(Cipher.ENCRYPT_MODE, loadPublicKey("/keylecordonbleu/public.key", "RSA"));

byte[] arrDecryptedKeyBytes = objCipherTunkicloud.doFinal(userName.getBytes(StandardCharsets.UTF_8));
log.error("SECURITY - key en array de bytes");

String tkn = new String(arrDecryptedKeyBytes);

userName = URLEncoder.encode(tkn, "ISO-8859-1");
