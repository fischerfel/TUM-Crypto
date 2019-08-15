// Get key from cert
CertificateFactory fact = CertificateFactory.getInstance("X.509", new org.bouncycastle.jce.provider.BouncyCastleProvider());
X509Certificate cer = (X509Certificate) fact.generateCertificate(new FileInputStream("/home/administrator/Downloads/cert_1.txt"));
PublicKey key = cer.getPublicKey();

// or read key in from pem file
PublicKey publicKey = ManifestUtils.publicKeyFromPemFile(new FileReader("/home/administrator/Downloads/publickey.txt"));

// Decrypt the signature
Cipher asymmetricCipher
            = Cipher.getInstance("RSA/ECB/PKCS1Padding", new org.bouncycastle.jce.provider.BouncyCastleProvider());
asymmetricCipher.init(Cipher.DECRYPT_MODE, publicKey);
byte[] plainText = asymmetricCipher.doFinal(
            IOUtils.toByteArray(new FileInputStream("/home/administrator/Downloads/signature.sign")));

// print as hex
System.out.println(Hex.encodeHexString(plainText));

// Print the ans1 nicely - ish
ASN1InputStream input = new ASN1InputStream(plainText);
ASN1Primitive p;
while ((p = input.readObject()) != null) {
    System.out.println(ASN1Dump.dumpAsString(p));
}
