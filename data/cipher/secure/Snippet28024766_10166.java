X509Certificate c = (X509Certificate)keyStore.getCertificate(alias);
String serialNumber = c.getSerialNumber().toString();
Key privateKey = (Key) keyStore.getKey(DSCName, null);
Certificate[] chain = keyStore.getCertificateChain(DSCName);
DataOutputStream fout = new DataOutputStream(outstream);
  Provider p = keyStore.getProvider();
  String myData = "data to encrypt";
  PublicKey publicKey =  c.getPublicKey();

String cipherText = null;                   
final Cipher aesCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding",keyStore.getProvider().getName());
aesCipher.init(Cipher.ENCRYPT_MODE, privateKey);
byte[] byteDataToRate = myData.getBytes();
byte[] byteCipherText = aesCipher.doFinal(byteDataToRate);
cipherText = new BASE64Encoder().encode(byteCipherText);
System.out.println("cipherText:" + cipherText);
// ----------------------------
final Cipher aesCipher1 = Cipher.getInstance("RSA/ECB/PKCS1Padding",keyStore.getProvider().getName());
aesCipher1.init(Cipher.DECRYPT_MODE, public Key);

byte[] byteDecryptedText = aesCipher1.doFinal(byteCipherText);
System.out.println(byteDecryptedText);
String decryptedText = new String(byteDecryptedText);
