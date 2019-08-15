Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.ENCRYPT_MODE, privKey);
byte[] ret = cipher.doFinal(asn);
