/* Load Public key from certificate file */
CertificateFactory fact = CertificateFactory.getInstance("X.509");
FileInputStream is = new FileInputStream("cert.pem");
Certificate cer = fact.generateCertificate(is);
PublicKey publicKey = cer.getPublicKey();

/* Encrypt data using public key */
Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.ENCRYPT_MODE, publicKey);
String encryptedData = Base64.encodeBase64String(cipher.doFinal(data.getBytes("UTF-8")));
