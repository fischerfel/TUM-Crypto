String m_sSendStringEncrypt = "";
try{
    String m_sReqSendString = "StringToBeEncrypted";
    String m_sSalt = "test"; // not to change
    m_sSendStringEncrypt = encrypt(m_sReqSendString, m_sSalt);
}catch(Exception e){
    System.out.println("Exception:: " + e);
}


private static final String ALGORITHM = "AES";
private static final int ITERATIONS = 2;
private static final byte[] keyValue = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05,    0x06, 0x07, 0x08, 0x09,0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f }; // not to change

public static String encrypt(String value, String salt) throws Exception {
     Key key = generateKey();
     Cipher c = Cipher.getInstance(ALGORITHM);  
     c.init(Cipher.ENCRYPT_MODE, key);

     String valueToEnc = null;
     String eValue = value;
     for (int i = 0; i < ITERATIONS; i++) {
          valueToEnc = salt + eValue;
          byte[] encValue = c.doFinal(valueToEnc.getBytes());
          eValue = new BASE64Encoder().encode(encValue);
     }
     return eValue;
}
private static Key generateKey() throws Exception {
     Key key = new SecretKeySpec(keyValue, ALGORITHM);
     return key;
}
