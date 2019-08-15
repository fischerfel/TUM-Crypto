public byte[] Encrypt(byte[] data)
{
  byte[] bCertificate = Value;
  //Get Public Key from the certificate
  X509CertImpl x509Cert = new X509CertImpl(bCertificate);
  PublicKey publicKey = x509Cert.getPublicKey();
  RSAPublicKey rsaPublickey = (RSAPublicKey) publicKey;

  byte[] cipher = new byte[256];
  byte[] paddingData = new byte[]{(byte) 0x9a, (byte) 0x72, (byte) 0x7f, 
      (byte)0x3b, (byte) 0xe4, (byte) 0x9d, (byte) 0x47, (byte) 0x03, 
      (byte) 0x2f, (byte) 0x15,(byte) 0x5f, (byte) 0x2f, (byte) 0x8f, 
      (byte) 0xc0, (byte) 0xf4, (byte) 0x39};

  byte[] tempData = null;

  AsymmetricBlockCipher eAsymmetricBlockCipher = new OAEPEncoding(
      new RSAEngine(), new SHA256Digest(), paddingData);
  BigInteger eModulus = new BigInteger(1, rsaPublickey.getModulus()
      .toByteArray());
  BigInteger eExponent = new BigInteger("1", 16);
  RSAKeyParameters rsaKeyParams = new RSAKeyParameters(false, eModulus, 
      eExponent);

  eAsymmetricBlockCipher.init(true, rsaKeyParams);
  tempData = eAsymmetricBlockCipher.processBlock(data, 0, data.length);            

  Cipher encryptionCipher = Cipher.getInstance("RSA/ECB/NoPadding");
  encryptionCipher.init(Cipher.ENCRYPT_MODE, publicKey);
  cipher = encryptionCipher.doFinal(tempData );

  return cipher;
}
