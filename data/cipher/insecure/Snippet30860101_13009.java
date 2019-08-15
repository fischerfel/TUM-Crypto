String objVal=<the json>;
SecretKeySpec lKeySpec = new SecretKeySpec(lKey.getBytes("UTF8"),"Blowfish");
Cipher lCipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
lCipher.init(Cipher.ENCRYPT_MODE, lKeySpec);
byte[] lPassword = objVal.getBytes("UTF8");
byte[] lEncryptPassword = lCipher.doFinal(lPassword);
String lEncryptString = new BASE64Encoder().encode(lEncryptPassword);
StringBuffer nString = new StringBuffer();
for (int i = 0; i < lEncryptString.length(); i++) {
int a = lEncryptString.charAt(i);
if (a != 13 && a != 10 && !lEncryptString.substring(i, i + 1).equals(" ")){
nString.append(lEncryptString.charAt(i));
}
return nString.toString();
