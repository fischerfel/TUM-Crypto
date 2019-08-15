WServiceSoap ws = new WService().getWServiceSoap();
MessageDigest md = MessageDigest.getInstance("md5");

byte[] digestOfPassword = md.digest("12345678".getBytes("utf-8"));

final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
for (int j = 0, k = 16; j < 8;) {
  keyBytes[k++] = keyBytes[j++];
}

SecretKey opKey = new SecretKeySpec(keyBytes, "DESede");        
byte[] opIV = { 0, 0, 0, 1, 2, 3, 4, 5 };       
Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");

c.init(Cipher.ENCRYPT_MODE, opKey, new IvParameterSpec(opIV));

byte[] encrypted = c.doFinal(
    ClientDirectOperacionCTMS.DATOS_OPERACION.getBytes("UTF-8"));

String encryptedDatosOperacion= Base64.encodeBase64String(encrypted);
String result= ws.operacionCTMS(encryptedDatosOperacion);
System.out.println(result);
