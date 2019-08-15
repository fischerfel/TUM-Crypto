import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.sql.SQLException;

import javax.crypto.Cipher;

    public class RSAKeyPack implements Serializable {

      private static final long serialVersionUID = 2L;
      PublicKey publicKey;
      PrivateKey privateKey;
        //KeyPairGenerator keyPairGenerator;
        transient KeyPairGenerator keyPairGenerator;

        private  void getGenerator() throws NoSuchAlgorithmException {
           if (keyPairGenerator == null) {
               keyPairGenerator = KeyPairGenerator.getInstance("RSA");
               keyPairGenerator.initialize(1024); //1024 used for normal securities
               KeyPair keyPair = keyPairGenerator.generateKeyPair();  
               publicKey = keyPair.getPublic();  
               privateKey = keyPair.getPrivate();
           }

        }
        public RSAKeyPack()
        {

            try {
                getGenerator();
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            /*try 
            {

                keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(2048); //1024 used for normal securities
                KeyPair keyPair = keyPairGenerator.generateKeyPair();  
                 publicKey = keyPair.getPublic();  
                privateKey = keyPair.getPrivate();          
            } 
            catch (NoSuchAlgorithmException e) 
            {
                e.printStackTrace();
            }*/
        }

        public PublicKey getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(PublicKey publicKey) {
            this.publicKey = publicKey;
        }

        public PrivateKey getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(PrivateKey privateKey) {
            this.privateKey = privateKey;
        }



        public   BigInteger  getParamModulus(PublicKey publickey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
        {

                 KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
                 RSAPublicKeySpec rsaPubKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);  
                 //RSAPrivateKeySpec rsaPrivKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);  
                 System.out.println("PubKey Modulus : " + rsaPubKeySpec.getModulus());


            return rsaPubKeySpec.getModulus();
           }  

        public   BigInteger  getParamExponent(PublicKey publickey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
        {

                 KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
                 RSAPublicKeySpec rsaPubKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);  
                 //RSAPrivateKeySpec rsaPrivKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);  
                 System.out.println("PubKey Modulus : " + rsaPubKeySpec.getPublicExponent());


            return rsaPubKeySpec.getPublicExponent();
           }  


         public static PublicKey readPublicKey(BigInteger modulus,BigInteger exponent) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException{  


                  //Get Public Key  
                  RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, exponent);  
                  KeyFactory fact = KeyFactory.getInstance("RSA");  
                  PublicKey publicKey = fact.generatePublic(rsaPublicKeySpec);  
                  return publicKey;  


         }   


         public  byte[] encryptData(byte[] data,PublicKey pubKey) throws IOException {  


                 byte[] encryptedData = null;  
                 try {  

                        Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");  
                        cipher.init(Cipher.ENCRYPT_MODE, pubKey);

                        System.out.println("data key length after encryption"+data.length);
                        encryptedData = cipher.doFinal(data);  
                        System.out.println("data key length after encryption"+encryptedData.length);

                 } catch (Exception e) {  
                     System.out.println("----------------ENCRYPTION ABANDONED!!!------------"); 
                        e.printStackTrace();  
                 }   


                 return (encryptedData);  
             }  


         public    byte[] decryptData(byte[] data,PrivateKey privateKey) throws IOException {  

              byte[] descryptedData = null;  

              try {  

               Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");

               cipher.init(Cipher.DECRYPT_MODE, privateKey);  
               descryptedData = cipher.doFinal(data);  
               System.out.println("data key length after decryption     "+data.length);

              } catch (Exception e) {  
               e.printStackTrace();  
              }   

              return descryptedData ;

             }  
    }
