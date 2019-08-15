cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "SunMSCAPI");
cipher.init(Cipher.ENCRYPT_MODE, mscapiPrivKey);
byte[] ret = cipher.doFinal(asn1);
