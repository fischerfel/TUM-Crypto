import java.io.ByteArrayInputStream;    
import java.io.ByteArrayOutputStream;    
import javax.crypto.Cipher;    
import javax.crypto.CipherInputStream;    
import javax.crypto.CipherOutputStream;    
import javax.crypto.spec.IvParameterSpec;    
import javax.crypto.spec.SecretKeySpec;    
import javax.xml.bind.DatatypeConverter;

public class FileEncryption {

    //Initial Vector
    public static final byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };      

    //EncryptAndDecrypt String -> Input : PlainText + Return : CipherText+DecipherText
    public static String encryptString(String src) throws Exception
    {
        String dst="";
        //Not Input!
        if(src == null || src.length()==0)
            return "";

        //Encryption Setting
        byte[] k="Multimediaproces".getBytes();
        SecretKeySpec Key = new SecretKeySpec(k,"AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        Cipher encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE,Key,ivspec);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CipherOutputStream cout = new CipherOutputStream(baos,encryptCipher);
        cout.write(src.getBytes());
        cout.flush();               //ByteOutputStream -> Write Encryption Text
        cout.close();           
        dst = DatatypeConverter.printHexBinary(baos.toByteArray());
        return dst;
    }   
    //String src -> EncryptedData
    public static String decryptString(String src) throws Exception 
    {
        //src value is Encrypted Value!
        //So, src value -> Not Byte!
        //String dst="";
        byte[] encryptedBytes = src.getBytes();         
        //Not Input!
        if(src == null || src.length()==0)
            return "";          
        //Decryption Setting
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        byte[] k="Multimediaproces".getBytes();
        SecretKeySpec Key = new SecretKeySpec(k,"AES");
        Cipher decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        decryptCipher.init(Cipher.DECRYPT_MODE,Key,ivspec); 

        //ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream bais = new ByteArrayInputStream(encryptedBytes);
        CipherInputStream cin = new CipherInputStream(bais,decryptCipher);
        //CipherOutputStream cout = new CipherOutputStream(baos,decryptCipher);
        //cout.write(src.getBytes());
        byte[] buf = new byte[1024];
        int read;
        while((read=cin.read(buf))>=0)  //reading encrypted data!
        {
            cin.read(buf,0,read);     //writing decrypted data!
            //read = cin.read(buf);
        }
        // closing streams
        //baos.write(decryptCipher.doFinal());
        //return baos.toString();
        //cin.close(); 
        return bais.toString();
    }
}
