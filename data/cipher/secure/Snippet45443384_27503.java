public class AESCCMEncryption {
     public static int AES_KEY_SIZE = 128 ;
     public static int TAG_BIT_LENGTH = 128 ;
     public static String ALGO_TRANSFORMATION_STRING = "AES/CCM/PKCS5Padding" ;
     public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {


    SecretKey aesKey = null ;
    String message="messageToEncrypt";
    try {
        KeyGenerator keygen = KeyGenerator.getInstance("AES") ;  
        keygen.init(AES_KEY_SIZE) ; 
        aesKey = keygen.generateKey() ;
    } catch(NoSuchAlgorithmException noSuchAlgoExc) { System.out.println("Key being request is for AES algorithm, but this cryptographic algorithm is not available in the environment "  + noSuchAlgoExc) ; System.exit(1) ; }
    byte[] encryptedText = aesEncrypt(message, aesKey) ;
    byte[] decryptedText = aesDecrypt(encryptedText, aesKey) ; 

    System.out.println("Decrypted text " + new String(decryptedText)) ;

}
     public static byte[] aesEncrypt(String message, SecretKey aesKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
         Cipher c = null ;
                c = Cipher.getInstance(ALGO_TRANSFORMATION_STRING); 
                c.init(Cipher.ENCRYPT_MODE, aesKey) ;
                byte[] cipherTextInByteArr = null ;
               cipherTextInByteArr = c.doFinal(message.getBytes()) ;
              return cipherTextInByteArr ;
 } 
     public static byte[] aesDecrypt(byte[] encryptedMessage, SecretKey aesKey) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
         Cipher c = null ;
              c = Cipher.getInstance(ALGO_TRANSFORMATION_STRING); // Transformation specifies algortihm, mode of operation and padding
              c.init(Cipher.DECRYPT_MODE, aesKey) ;
              byte[] plainTextInByteArr = null ;
              plainTextInByteArr = c.doFinal(encryptedMessage) ;
          return plainTextInByteArr ;
       }
}
