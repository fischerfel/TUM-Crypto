KeyStore ks = null;
try {
    ks = KeyStore.getInstance(KeyStore.getDefaultType()); 
    InputStream is = new FileInputStream("./raw/./akeystore");
    ks.load(is, "a".toCharArray());
} catch (Exception e) {
    e.printStackTrace();
}

Certificate cert = ks.getCertificate("akeypaircer");
RSAPublicKey publicKey = (RSAPublicKey)cert.getPublicKey();
Cipher c = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
c.init(Cipher.ENCRYPT_MODE, publicKey);

byte[] decodedString = Base64.decodeBase64("how's it going?".getBytes("UTF-8"));
byte [] cipher = c.doFinal(decodedString);

RSAPrivateKey privateKey = (RSAPrivateKey)ks.getKey("akeypair","a".toCharArray());
c.init(Cipher.DECRYPT_MODE, privateKey);

byte [] decrypted_cipher = c.doFinal(cipher);
byte[] encodedBytes = Base64.encodeBase64(decrypted_cipher);

System.out.println(new String(encodedBytes, "UTF-8"));
