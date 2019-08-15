import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

 public String EncryptData(String data, String skey) throws Exception {      
    String encryptedData = "";        

    try{
       byte [] bData = data.getBytes();
       String alg = "AES/ECB/NoPadding"; 
       SecretKey key = new SecretKeySpec(skey.getBytes(), alg.replaceFirst("/.*", "")); 
       Cipher cipher = Cipher.getInstance(alg);
       cipher.init(Cipher.ENCRYPT_MODE, key);
       byte[] encoded = cipher.doFinal(bData);          
       encryptedData = bytesToHex(encoded);
    }
    catch(Exception e){
        throw e;
    }
    return encryptedData;
}



public String DecryptData(String hexString, String skey) throws Exception {

    String decryptedData = "";
    try{
       byte [] bData =  convToBinary(hexString);

       String alg = "AES/ECB/NoPadding";            
       SecretKey key = new SecretKeySpec(skey.getBytes(), alg.replaceFirst("/.*", ""));           
       Cipher cipher = Cipher.getInstance(alg);
       cipher.init(Cipher.DECRYPT_MODE, key);
       byte[] decoded = cipher.doFinal(bData);
       decryptedData = new String(decoded);
    }
    catch(Exception e){
        throw e;
    }
    return decryptedData;
}
