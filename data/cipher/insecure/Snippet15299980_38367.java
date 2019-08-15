public class StringCryptor 
{
    private static final String CIPHER_ALGORITHM = "AES";
    private static final String RANDOM_GENERATOR_ALGORITHM = "SHA1PRNG";
    private static final int RANDOM_KEY_SIZE = 128;
    // Private key already generated with generatekey()
    static String PKEY= "15577737BBD910E794A6B3C250678DAF";
    // Convert PKEY to byte[]
    static byte[] secretKey = toByte(PKEY);

    // Encrypts string and encode in Base64
    public static String encrypt( String password, String data ) throws Exception 
    {
         byte[] clear = data.getBytes();
         SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, CIPHER_ALGORITHM );
         Cipher cipher = Cipher.getInstance( CIPHER_ALGORITHM );
         cipher.init( Cipher.ENCRYPT_MODE, secretKeySpec );
         byte[] encrypted = cipher.doFinal( clear );
         String encryptedString = Base64.encodeToString( encrypted, Base64.DEFAULT );
         return encryptedString;
    }

    // Decrypts string encoded in Base64
    public static String decrypt( String password, String encryptedData ) throws Exception 
    {
         SecretKeySpec secretKeySpec = new SecretKeySpec( secretKey, CIPHER_ALGORITHM );
         Cipher cipher = Cipher.getInstance( CIPHER_ALGORITHM );
         cipher.init( Cipher.DECRYPT_MODE, secretKeySpec );
         byte[] encrypted = Base64.decode( encryptedData, Base64.DEFAULT );
         byte[] decrypted = cipher.doFinal( encrypted );
         return new String( decrypted );
    }


    // Convert String To Hexa
    public static String toHex(byte[] buf) { 
        if (buf == null)  
            return "";    
        StringBuffer result = new StringBuffer(2*buf.length);  
        for (int i = 0; i < buf.length; i++) {
           appendHex(result, buf[i]);       
        }        
       return result.toString();
    }
  // Convert hex To byte
  public static byte[] toByte(String hexString) { 
        int len = hexString.length()/2;      
        byte[] result = new byte[len];        
        for (int i = 0; i < len; i++)               
            result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();   
        return result;
  }

  private final static String HEX = "0123456789ABCDEF";
  private static void appendHex(StringBuffer sb, byte b) {
      sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
  }

//public static byte[] generateKey( byte[] seed ) throws Exception
    //{
    //KeyGenerator keyGenerator = KeyGenerator.getInstance( CIPHER_ALGORITHM );
        //SecureRandom secureRandom = SecureRandom.getInstance( RANDOM_GENERATOR_ALGORITHM );
        //secureRandom.setSeed( seed );
        //keyGenerator.init( RANDOM_KEY_SIZE, secureRandom );
        //SecretKey secretKey = keyGenerator.generateKey();
        //return secretKey.getEncoded();
    //}
}
