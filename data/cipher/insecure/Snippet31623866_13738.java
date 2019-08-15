import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESSecurityUtil {

    private static final String AES = "AES";  
    private static final String CHARSET_NAME = "utf-8";  


    private static SecretKeySpec getKey(String password) throws NoSuchAlgorithmException{  

        KeyGenerator kgen = KeyGenerator.getInstance(AES);  
        SecureRandom random=SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(password.getBytes());
        kgen.init(128, random);    

        SecretKey secretKey = kgen.generateKey();  
        byte[] enCodeFormat = secretKey.getEncoded();    
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, AES);   
        return key;  
    }  


    public static String encode(String str, String password)  
    {  
        byte[] arr = encodeToArr(str, password);  
        return byteArrToString(arr);  
    }  


    private static byte[] encodeToArr(String str, String password)  
    {  
        try
        {  
            Cipher cipher = Cipher.getInstance(AES);
            byte[] byteContent = str.getBytes(CHARSET_NAME);  

            cipher.init(Cipher.ENCRYPT_MODE, getKey(password));
            byte[] result = cipher.doFinal(byteContent);    
            return result;  
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }    
        return null;  
    }  


    public static String decode(String hexStr, String password){  
        byte[] arr = string2ByteArr(hexStr);  
        return decode(arr, password);  
    }  


    private static String decode(byte[] arr, String password)  {  
        try{  

            Cipher cipher = Cipher.getInstance(AES);  
            cipher.init(Cipher.DECRYPT_MODE, getKey(password));

            byte[] result = cipher.doFinal(arr);  
            return new String(result, CHARSET_NAME);  
        }catch (Exception e){  
            e.printStackTrace();  
        }  
        return null;  
    }  



    private static String byteArrToString(byte[] arr)  {  
        StringBuffer sb = new StringBuffer();   
        for (int i = 0; i <arr.length; i++)  {    
            String s = Integer.toString(arr[i] + 128, 16);  
            if (s.length() == 1){  
                s = "0" + s;  
            }    
            sb.append(s);  
        }  

        return sb.toString().toUpperCase();  
    }  


    private static byte[] string2ByteArr(String s)  {  
        s = s.toUpperCase();  
        String str = "0123456789ABCDEF";    
        byte[] arr = new byte[s.length() / 2];   
        for (int i = 0; i <arr.length; i++){  
            char s1 = s.charAt(i * 2);  
            char s2 = s.charAt(i * 2 + 1);    
            int tmp1 = str.indexOf(s1) * 16;  
            int tmp2 = str.indexOf(s2);    
            arr[i] = (byte) (tmp1 + tmp2 - 128);  
        }    
        return arr;  
    }  

    public static void main(String[] args) throws Exception  {
        System.out.println(decode("03AB8A3B85AFDD3926850B14C1BFF608", "imcc"));
        String keyStr = "UITN25LMUQC436IM";  

        String plainText = "this is a string will be AES_Encrypt";

        String encText = encode(plainText,keyStr);
        String decString = decode(encText,keyStr); 

        System.out.println(encText); 
        System.out.println(decString); 
    }
}
