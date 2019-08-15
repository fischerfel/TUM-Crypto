public  String getAccessToken(String deviceId,String userId ){
final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";
final String ALGORITHM="RSA";
protected final String CHARCTER_ENCODING = "UTF-8";

String timeStamp=String.valueOf(new java.util.Date().getTime());
String accessTokenToEncript=userId +":##"+beanInput.getPassword()+":##"+deviceId+":##"+timeStamp;
acccessToken=FAR_CIPHER.encrypt(inputMessage);

X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(IOUtils.toByteArray(new FileInputStream(PUBLIC_KEY_FILE_PATH)));
Cipher cipher = Cipher.getInstance(TRANSFORMATION);
cipher.init(Cipher.ENCRYPT_MODE, KeyFactory.getInstance(ALGORITHM).generatePublic(x509EncodedKeySpec));
return Base64.encodeBase64String(cipher.doFinal(rawText.getBytes(CHARCTER_ENCODING)));

}
