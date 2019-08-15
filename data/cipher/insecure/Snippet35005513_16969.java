import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class JEncrytion
{   
    private static final char[] hexDigits = {
        '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'
    };
       /**
     * Returns a string of hexadecimal digits from a byte array. Each
     * byte is converted to 2 hex symbols.
     * <p>
     * If offset and length are omitted, the whole array is used.
     */
    public static String toString(byte[] ba, int offset, int length) {
        char[] buf = new char[length * 2];
        int j = 0;
        int k;

        for (int i = offset; i < offset + length; i++) {
            k = ba[i];
            buf[j++] = hexDigits[(k >>> 4) & 0x0F];
            buf[j++] = hexDigits[ k        & 0x0F];
        }
        return new String(buf);
    }
    public static String toString(byte[] ba) {
        return toString(ba, 0, ba.length);
    }
      /**
     * Returns the number from 0 to 15 corresponding to the hex digit <i>ch</i>.
     */
    public static int fromDigit(char ch) {
        if (ch >= '0' && ch <= '9')
            return ch - '0';
        if (ch >= 'A' && ch <= 'F')
            return ch - 'A' + 10;
        if (ch >= 'a' && ch <= 'f')
            return ch - 'a' + 10;

        throw new IllegalArgumentException("invalid hex digit '" + ch + "'");
    }
    /**
     * Returns a byte array from a string of hexadecimal digits.
     */
    public static byte[] fromString(String hex) {
        int len = hex.length();
        byte[] buf = new byte[((len + 1) / 2)];

        int i = 0, j = 0;
        if ((len % 2) == 1)
            buf[j++] = (byte) fromDigit(hex.charAt(i++));

        while (i < len) {
            buf[j++] = (byte) ((fromDigit(hex.charAt(i++)) << 4) |
                                fromDigit(hex.charAt(i++)));
        }
        return buf;
    }
    //Add padding to make key 24 bytes length i.e. 192 bits length
    public static byte [] addPaddingToKey (byte [] keyBytes) {
        byte[] key;
        int keyLength = keyBytes.length;
        int paddingLenght = 24 - keyLength;
        if (paddingLenght != 0) {
            key = new byte[24];
            //copy original key
            System.arraycopy(keyBytes, 0, key, 0, keyLength);
            //Adding 0 as padding for remaining
            System.arraycopy(keyBytes, 0, key, keyLength, paddingLenght);
        } else {
            key = keyBytes;
        }
        return key;
    }
    //Add padding to input value to 
    public static byte[] addPadding(byte[] inData, int offset, int len) {

        byte[] bp = null;
        int padChars = 8;               // start with max padding value
        int partial = (len + 1) % padChars;     // calculate how many extra bytes exist
        if (partial == 0) padChars = 1;         // if none, set to only pad with length byte
        else padChars = padChars - partial + 1; // calculate padding size to include length

        bp = new byte[len + padChars];
        bp[0] = Byte.parseByte(Integer.toString(padChars));
        System.arraycopy(inData, offset, bp, 1, len);
        return bp;
    }
    //remove padding added while decryption
     public static byte[] removePadding(byte[] inData) {

            byte[] bp = null;
            int dataLength = 0;
            int padLength = 0;
            padLength = inData[0];
            dataLength = inData.length - padLength;
            bp = new byte[dataLength];
            System.arraycopy(inData, 1, bp, 0, dataLength);      
            return bp;
      } 
     //Get SecretKey object 
    public static SecretKey getSecretKey(String secretString) {
            SecretKey myDesKey = null;
            try {
                //get bytes from key
                byte [] keyBytes = secretString.getBytes();
                //add padding to key to meet 24 bytes requirement
                byte [] bytes = addPaddingToKey(keyBytes);
                //Generate DES key 
                myDesKey = new SecretKeySpec(bytes,"DESede" );

            }catch (Exception e) {
                e.printStackTrace();    
            }
            return myDesKey;
    }
     //encrypt data
     public static byte[] encrypt(byte[] text, String secretString) {
         byte[] textEncrypted = null;
            try {
                 //Generate DES key 
                SecretKey myDesKey = getSecretKey(secretString);

                Cipher desCipher;
                // Create the cipher  for DES-EDE3
                desCipher = Cipher.getInstance("DESede/ECB/NoPadding");

                // Initialize the cipher for encryption
                desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);

                //Add padding to make it multiple of 8
                text  = addPadding (text, 0 , text.length);

                // encrypt the text
                textEncrypted = desCipher.doFinal(text);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return textEncrypted;

          } 
     //Decrypt given encrypted bytes
    public static byte[] decrypt (byte [] textEncrypted, String secretString){
        byte[] textDecrypted = null;
        try {            
            //Generate DES key 
            SecretKey myDesKey = getSecretKey(secretString);

            Cipher desCipher;
            // Create the cipher  for DES-EDE3
            desCipher = Cipher.getInstance("DESede/ECB/NoPadding");


            // Initialize the same cipher for decryption
            desCipher.init(Cipher.DECRYPT_MODE, myDesKey);

            // Decrypt the text
            textDecrypted = desCipher.doFinal(textEncrypted);

            //remove padding which was added during encryption
            textDecrypted  = removePadding (textDecrypted);

        }catch (Exception e) {
            e.printStackTrace();    
        }
        return textDecrypted;
    } 
    public static void main(String[] argv) {        
        try{
            String secretString = "hg34sdfg4ertertr";
            String plainTextString = "{\"name\":\"Test\"}";
            byte[] plainText = plainTextString.getBytes();
            //Encrypt given plain text
            byte [] encryptedText = encrypt(plainText, secretString);
            //Convert into hex

            String hexStr = toString(encryptedText);
            System.out.println("Encrypted String "+hexStr);        
            byte [] decryptedText = decrypt(encryptedText, secretString);
            System.out.println(" decrypted string "+new String (decryptedText));

        }catch(Exception e){
            e.printStackTrace();
        } 

    }
}
