import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

...

public static String encrypt(String content, String password) {  
    try {             
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));

            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key); 
            byte[] byteContent = content.getBytes("utf-8"); 
            byte[] result = cipher.doFinal(byteContent);                   
            return Base64.encode(result);  
    } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
    } catch (NoSuchPaddingException e) {  
            e.printStackTrace();  
    } catch (InvalidKeyException e) {  
            e.printStackTrace();  
    } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
    } catch (IllegalBlockSizeException e) {  
            e.printStackTrace();  
    } catch (BadPaddingException e) {  
            e.printStackTrace();  
    }
    return null;  
}
