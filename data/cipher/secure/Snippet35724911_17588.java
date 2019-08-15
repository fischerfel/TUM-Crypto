import java.io.Serializable;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAKeyGenParameterSpec;
import javax.crypto.BadPaddingException;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class NewRSA implements Serializable{

   private KeyPair keys ;


   public NewRSA(){
       this.keys=null;
   }
   public NewRSA(KeyPair keys)
   {
       this.keys=keys;
   }
    public KeyPair getKPair()
    {
        return keys;
    }
     public void setKPair(KeyPair keys)
    {
        this.keys=keys;
    }
  public  KeyPair generateRsaKeyPair(int keySize, BigInteger publicExponent)
  {

    try
    {
      KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
      RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(keySize, publicExponent);
      keyGen.initialize(spec);
      keys = keyGen.generateKeyPair();
    }
    catch(Exception e)
    {

    }
    return keys;
  }



    public  byte[] rsaEncrypt(byte[] original, PublicKey key) throws InvalidKeyException, IllegalBlockSizeException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException
  {


      Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
      cipher.init(Cipher.ENCRYPT_MODE, key);
      return cipher.doFinal(original);



  }


  public static byte[] rsaDecrypt(byte[] encrypted, PrivateKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
  {

      Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
      cipher.init(Cipher.DECRYPT_MODE, key);
      return cipher.doFinal(encrypted);


  }
}
