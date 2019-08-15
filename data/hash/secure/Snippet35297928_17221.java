CertificateFactory cf = CertificateFactory.getInstance("X.509");
InputStream in = new ByteArrayInputStream(myCert);
X509Certificate cert = (X509Certificate) cf.generateCertificate(in);

// calculate original message hash
MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
sha256.update(myData);
byte[] md = sha256.digest();

// decrypt signature with cert's public key
Cipher rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
rsa.init(Cipher.DECRYPT_MODE, cert.getPublicKey());
byte[] x = rsa.doFinal(mySignedData);

// compare if results are equal
boolean verification = Arrays.equals(md, x);
