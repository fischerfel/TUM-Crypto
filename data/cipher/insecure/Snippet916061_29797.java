static
{
   Security.addProvider(new BouncyCastleProvider());
}

protected String encrypt(byte[] keyData, byte[] data) throws Exception {
   X509EncodedKeySpec keyspec = new X509EncodedKeySpec(keyData);
   KeyFactory kf = KeyFactory.getInstance("RSA", "BC");
   PublicKey pk = kf.generatePublic(keyspec);
   Cipher rsa =  Cipher.getInstance("RSA/None/OAEPWithSHA1AndMGF1Padding", "BC");
   rsa.init(Cipher.ENCRYPT_MODE, pk);
   byte[] output = rsa.doFinal(data);
   String result = base64EncodeBytes(output);
   return result;
}
