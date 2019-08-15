validate(cipherText)
 private static final byte[] desKeyData = {
        (byte)0xA2, (byte)0x15, (byte)0x37, (byte)0x07, (byte)0xCB, (byte)0x62, 
        (byte)0xC1, (byte)0xD3, (byte)0xF8, (byte)0xF1, (byte)0x97, (byte)0xDF,
        (byte)0xD0, (byte)0x13, (byte)0x4F, (byte)0x79, (byte)0x01, (byte)0x67, 
        (byte)0x7A, (byte)0x85, (byte)0x94, (byte)0x16, (byte)0x31, (byte)0x88 };


byte[] bCipherText = Base64.decode(CipherText);


Cipher oC3des = Cipher.getInstance("DESede/CBC/PKCS5Padding");
SecretKeySpec    oKey = new SecretKeySpec(s_desKeyData , "DESede");
IvParameterSpec oIvSpec = new IvParameterSpec(s_baIV);

oC3des.init(Cipher.DECRYPT_MODE, oKey, oIvSpec);
byte[] plaintext = oC3des.doFinal(bCipherText );

return new String(plainText, Charset.forName("UTF-8"));
