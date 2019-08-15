import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class Encrypt3DES {

     private byte[] key;
     private byte[] initializationVector;

    public Encrypt3DES(){

    }

    public String encryptText(String plainText, String key) throws Exception{

         //----  Use specified 3DES key and IV from other source --------------
        byte[] plaintext = plainText.getBytes();
        byte[] myIV = key.getBytes();
        byte[] tdesKeyData = {(byte)0xA2, (byte)0x15, (byte)0x37, (byte)0x08, (byte)0xCA, (byte)0x62,
        (byte)0xC1, (byte)0xD2, (byte)0xF7, (byte)0xF1, (byte)0x93, (byte)0xDF,
        (byte)0xD2, (byte)0x15, (byte)0x4F, (byte)0x79, (byte)0x06, (byte)0x67,
        (byte)0x7A, (byte)0x82, (byte)0x94, (byte)0x16, (byte)0x32, (byte)0x95};

        Cipher c3des = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        SecretKeySpec    myKey = new SecretKeySpec(tdesKeyData, "DESede");
        IvParameterSpec ivspec = new IvParameterSpec(myIV);
           c3des.init(Cipher.ENCRYPT_MODE, myKey, ivspec);
        byte[] cipherText = c3des.doFinal(plaintext);
        sun.misc.BASE64Encoder obj64=new sun.misc.BASE64Encoder();
        return obj64.encode(cipherText);

    }

    public String decryptText(String encryptText, String key) throws Exception{


        byte[] initializationVector = key.getBytes();
        byte[] tdesKeyData = {(byte)0xA2, (byte)0x15, (byte)0x37, (byte)0x08, (byte)0xCA, (byte)0x62,
        (byte)0xC1, (byte)0xD2, (byte)0xF7, (byte)0xF1, (byte)0x93, (byte)0xDF,
        (byte)0xD2, (byte)0x15, (byte)0x4F, (byte)0x79, (byte)0x06, (byte)0x67,
        (byte)0x7A, (byte)0x82, (byte)0x94, (byte)0x16, (byte)0x32, (byte)0x95};


          byte[] encData = new sun.misc.BASE64Decoder().decodeBuffer(encryptText);
          Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
          SecretKeySpec myKey = new SecretKeySpec(tdesKeyData, "DESede");
          IvParameterSpec ivspec = new IvParameterSpec(initializationVector);
          decipher.init(Cipher.DECRYPT_MODE, myKey, ivspec);
          byte[] plainText = decipher.doFinal(encData);
          return new String(plainText);

    }
}
