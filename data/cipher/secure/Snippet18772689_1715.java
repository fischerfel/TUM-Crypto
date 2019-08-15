import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

 private final String characterEncoding = "UTF-8";    
     private final String cipherTransformation = "AES/CBC/PKCS5Padding";    
     private final String aesEncryptionAlgorithm = "AES"; 



 public String decrypt(String plainTextString, String SecretKey) throws 
     KeyException, 
     GeneralSecurityException, 
     GeneralSecurityException, 
     InvalidAlgorithmParameterException, 
     IllegalBlockSizeException, 
     BadPaddingException, 
     IOException{        

     byte[] cipheredBytes = Base64.decode(plainTextString, Base64.BASE64DEFAULTLENGTH);        
     byte[] keyBytes = getKeyBytes(SecretKey);        
     return new String(decrypt(cipheredBytes, keyBytes, keyBytes), characterEncoding);    
     }

public  byte[] decrypt(byte[] cipherText, byte[] key, byte [] initialVector) throws 
     NoSuchAlgorithmException, 
     NoSuchPaddingException, 
     InvalidKeyException, 
     InvalidAlgorithmParameterException, 
     IllegalBlockSizeException, 
     BadPaddingException    {        

     Cipher cipher = Cipher.getInstance(cipherTransformation);        
     SecretKeySpec secretKeySpecy = new SecretKeySpec(key, aesEncryptionAlgorithm);        
     IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);        
     cipher.init(Cipher.DECRYPT_MODE, secretKeySpecy, ivParameterSpec);        
     cipherText = cipher.doFinal(cipherText);        
     return cipherText;    
     }     

 public byte[] encrypt(byte[] plainText, byte[] key, byte [] initialVector) throws 
     NoSuchAlgorithmException, 
     NoSuchPaddingException, 
     InvalidKeyException, 
     InvalidAlgorithmParameterException, 
     IllegalBlockSizeException, 
     BadPaddingException    {        

     Cipher cipher = Cipher.getInstance(cipherTransformation);        
     SecretKeySpec secretKeySpec = new SecretKeySpec(key, aesEncryptionAlgorithm);        
     IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);        
     cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);        
     plainText = cipher.doFinal(plainText);        
     return plainText;    
     }     

     private byte[] getKeyBytes(String key) throws UnsupportedEncodingException{        
     byte[] keyBytes= new byte[16];        
     byte[] parameterKeyBytes= key.getBytes(characterEncoding);        
     System.arraycopy(parameterKeyBytes, 0, keyBytes, 0, Math.min(parameterKeyBytes.length, keyBytes.length));        
     return keyBytes;    
     }     

     public String encrypt(String plainText, String key) throws 
     UnsupportedEncodingException, 
     InvalidKeyException, 
     NoSuchAlgorithmException, 
     NoSuchPaddingException, 
     InvalidAlgorithmParameterException, 
     IllegalBlockSizeException, 
     BadPaddingException{        

     byte[] plainTextbytes = plainText.getBytes(characterEncoding);        
     byte[] keyBytes = getKeyBytes(key);        
     return Base64.encode(encrypt(plainTextbytes,keyBytes, keyBytes));    
     }   
