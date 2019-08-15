Provider pkcs11provider = new SunPKCS11("C:\\Users\\manishs525\\pkcs11.cfg");
Cipher rsaCipher2 = Cipher.getInstance("RSA/ECB/NoPadding", pkcs11provider);
rsaCipher2.init(Cipher.DECRYPT_MODE, privateKey);
byte[] paddedPlainText = rsaCipher2.doFinal(cipherText);

/* Ensure leading zeros not stripped */
if (paddedPlainText.length < keyLength / 8) {
    byte[] tmp = new byte[keyLength / 8];
    System.arraycopy(paddedPlainText, 0, tmp, tmp.length - paddedPlainText.length, paddedPlainText.length);
    System.out.println("Zero padding to " + (keyLength / 8));
    paddedPlainText = tmp;
}           

System.out.println("OAEP padded plain text: " + DatatypeConverter.printHexBinary(paddedPlainText));
// === changed the next line ===
PSource pSrc = (new PSource.PSpecified(iv));
// === changed the last two parameters to MGF1ParameterSpec.SHA256 and pSrc ===
OAEPParameterSpec paramSpec = new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, pSrc);   // where iv is byte array of length 32
RSAPadding padding = RSAPadding.getInstance(RSAPadding.PAD_OAEP_MGF1, keyLength / 8, new SecureRandom(), paramSpec);
byte[] plainText2 = padding.unpad(paddedPlainText);
