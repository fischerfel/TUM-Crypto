Security.addProvider(neworg.bouncycastle.jce.provider.BouncyCastleProvider());
byte[] input = "Abc123".getBytes();
Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
FileInputStream fin = new FileInputStream(new File("/test.cer"));
CertificateFactory f = CertificateFactory.getInstance("X.509");
X509Certificate certificate = (X509Certificate)f.generateCertificate(fin);
PublicKey pk = certificate.getPublicKey();
cipher.init(Cipher.ENCRYPT_MODE, pk, new SecureRandom());
byte[] cipherText = cipher.doFinal(input);
