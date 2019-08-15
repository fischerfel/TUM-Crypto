public class SSNDecrypt {


    public static String decrypt(String ssnString, byte[] keyBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        Cipher AesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // byte[] keyBytes = SSNDecrypt.convertHexToBinary(keyString);

          SecretKeySpec spec = new SecretKeySpec(keyBytes, "AES");
// TODO FIND OUT HOW TO ACCESS KEY FROM TEXT FILE

        byte[] iv = SSNDecrypt.convertHexToBinary(SSNDecrypt.getIV(ssnString));
        byte[] ssn = SSNDecrypt.convertHexToBinary(SSNDecrypt.getSSN(ssnString));



        AesCipher.init(Cipher.DECRYPT_MODE, spec, new IvParameterSpec(iv));
        return new String(AesCipher.doFinal(ssn), "UTF-8");
    }

    private   static byte[] convertHexToBinary(String hexString) {

        return DatatypeConverter.parseHexBinary(hexString);

//      int charCount = hexString.length();
//      byte[] out = new byte[charCount / 2];
//      for(int i = 0; i < charCount; i+= 2) {
//          out[i/2] = hexString.substring(i,  i + 1).get
//      }
    }


    private  static String getSSN(String cryptString) {
        int delimiterIndex = cryptString.indexOf(":");
        return cryptString.substring(0, delimiterIndex );

    }



    private static  String getIV(String cryptString) {
        int delimiterIndex = cryptString.indexOf(":");
        return cryptString.substring(delimiterIndex + 1 );
    }
}
