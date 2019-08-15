package org.owasp.crypto;

import java.security.*;
import java.security.cert.*;
import javax.crypto.*;
import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;

public class PublicKeyCryptography {

    public static void main(String[] args) {

    SymmetricEncrypt encryptUtil = new SymmetricEncrypt();
    String strDataToEncrypt = "Hello World";
    byte[] byteDataToTransmit = strDataToEncrypt.getBytes();

    // Generating a SecretKey for Symmetric Encryption
    SecretKey senderSecretKey = SymmetricEncrypt.getSecret();

    //1. Encrypt the data using a Symmetric Key
    byte[] byteCipherText =                 encryptUtil.encryptData(byteDataToTransmit,senderSecretKey,"AES");
    String strCipherText = new BASE64Encoder().encode(byteCipherText);


    //2. Encrypt the Symmetric key using the Receivers public key
    try{
    // 2.1 Specify the Keystore where the Receivers certificate has been imported
    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
    char [] password = "testpwd".toCharArray();
    java.io.FileInputStream fis = new         java.io.FileInputStream("/org/owasp/crypto/testkeystore.ks");
     ks.load(fis, password);
     fis.close();

     // 2.2 Creating an X509 Certificate of the Receiver
     X509Certificate recvcert ;
     MessageDigest md = MessageDigest.getInstance("MD5");
     recvcert = (X509Certificate)ks.getCertificate("testrecv");
     // 2.3 Getting the Receivers public Key from the Certificate
     PublicKey pubKeyReceiver = recvcert.getPublicKey();

     // 2.4 Encrypting the SecretKey with the Receivers public Key
     byte[] byteEncryptWithPublicKey =  encryptUtil.encryptData(senderSecretKey.getEncoded(),pubKeyReceiver,"RSA/ECB/PKCS1Padding");
      String strSenbyteEncryptWithPublicKey = new               BASE64Encoder().encode(byteEncryptWithPublicKey);

      // 3. Create a Message Digest of the Data to be transmitted
      md.update(byteDataToTransmit);
       byte byteMDofDataToTransmit[] = md.digest();

      String strMDofDataToTransmit = new String();
      for (int i = 0; i < byteMDofDataToTransmit.length; i++){
      strMDofDataToTransmit = strMDofDataToTransmit +    Integer.toHexString((int)byteMDofDataToTransmit[i] & 0xFF) ;
      }

      // 3.1 Message to be Signed = Encrypted Secret Key + MAC of the data to be transmitted
       String strMsgToSign = strSenbyteEncryptWithPublicKey + "|" +     strMDofDataToTransmit;

      // 4. Sign the message
      // 4.1 Get the private key of the Sender from the keystore by providing the         password set for the private key while creating the keys using keytool
     char[] keypassword = "send123".toCharArray();
      Key myKey =  ks.getKey("testsender", keypassword);
      PrivateKey myPrivateKey = (PrivateKey)myKey;

        // 4.2 Sign the message
       Signature mySign = Signature.getInstance("MD5withRSA");
     mySign.initSign(myPrivateKey);
        mySign.update(strMsgToSign.getBytes());
       byte[] byteSignedData = mySign.sign();

        // 5. The Values byteSignedData (the signature) and strMsgToSign (the data which was signed) can be sent across to the receiver

        // 6.Validate the Signature
        // 6.1 Extracting the Senders public Key from his certificate
        X509Certificate sendercert ;
        sendercert = (X509Certificate)ks.getCertificate("testsender");
          PublicKey pubKeySender = sendercert.getPublicKey();
        // 6.2 Verifying the Signature
       Signature myVerifySign = Signature.getInstance("MD5withRSA");
       myVerifySign.initVerify(pubKeySender);
       myVerifySign.update(strMsgToSign.getBytes());

       boolean verifySign = myVerifySign.verify(byteSignedData);
       if (verifySign == false)
       {
       System.out.println(" Error in validating Signature ");
        }

        else
    System.out.println(" Successfully validated Signature ");

      // 7. Decrypt the message using Recv private Key to get the Symmetric Key
      char[] recvpassword = "recv123".toCharArray();
     Key recvKey =  ks.getKey("testrecv", recvpassword);
     PrivateKey recvPrivateKey = (PrivateKey)recvKey;

    // Parsing the MessageDigest and the encrypted value
      String strRecvSignedData = new String (byteSignedData);
     String[] strRecvSignedDataArray = new String [10];
       strRecvSignedDataArray = strMsgToSign.split("|");
    int intindexofsep = strMsgToSign.indexOf("|");
    String strEncryptWithPublicKey = strMsgToSign.substring(0,intindexofsep);
    String strHashOfData = strMsgToSign.substring(intindexofsep+1);

    // Decrypting to get the symmetric key
    byte[] bytestrEncryptWithPublicKey = new      BASE64Decoder().decodeBuffer(strEncryptWithPublicKey);
    byte[] byteDecryptWithPrivateKey =           encryptUtil.decryptData(byteEncryptWithPublicKey,recvPrivateKey,"RSA/ECB/PKCS1Padding");

      // 8. Decrypt the data using the Symmetric Key
      javax.crypto.spec.SecretKeySpec secretKeySpecDecrypted = new javax.crypto.spec.SecretKeySpec(byteDecryptWithPrivateKey,"AES");
       byte[] byteDecryptText =  encryptUtil.decryptData(byteCipherText,secretKeySpecDecrypted,"AES");
       String strDecryptedText = new String(byteDecryptText);
       System.out.println(" Decrypted data is " +strDecryptedText);

     // 9. Compute MessageDigest of data + Signed message 
      MessageDigest recvmd = MessageDigest.getInstance("MD5");
       recvmd.update(byteDecryptText);
      byte byteHashOfRecvSignedData[] = recvmd.digest();

      String strHashOfRecvSignedData = new String();

       for (int i = 0; i < byteHashOfRecvSignedData.length; i++){
       strHashOfRecvSignedData = strHashOfRecvSignedData +    Integer.toHexString((int)byteHashOfRecvSignedData[i] & 0xFF) ;
     }
        // 10. Validate if the Message Digest of the Decrypted Text matches the Message  Digest of the Original Message
        if (!strHashOfRecvSignedData.equals(strHashOfData))
      {
         System.out.println(" Message has been tampered ");
       }

      }

      catch(Exception exp)
        {
        System.out.println(" Exception caught " + exp);
       exp.printStackTrace();
          }


            }

        }



package org.owasp.crypto;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import java.security.Key;

import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.security.InvalidAlgorithmParameterException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import sun.misc.BASE64Encoder;

/**
* @author Joe Prasanna Kumar
* This program provides the following cryptographic functionalities
* 1. Encryption using AES
* 2. Decryption using AES
* High Level Algorithm :
* 1. Generate a DES key (specify the Key size during this phase) 
* 2. Create the Cipher 
* 3. To Encrypt : Initialize the Cipher for Encryption
* 4. To Decrypt : Initialize the Cipher for Decryption
*/

public class SymmetricEncrypt {

String strDataToEncrypt = new String();
String strCipherText = new String();
String strDecryptedText = new String();
static KeyGenerator keyGen;
private static String strHexVal = "0123456789abcdef";

public static SecretKey getSecret(){
/**
 *  Step 1. Generate an AES key using KeyGenerator
 *          Initialize the keysize to 128 
 * 
 */

    try{
        keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);

        }

    catch(Exception exp)
    {
        System.out.println(" Exception inside constructor " +exp);
    }

    SecretKey secretKey = keyGen.generateKey();
    return secretKey;
}

/**
 *  Step2. Create a Cipher by specifying the following parameters
 *          a. Algorithm name - here it is AES
 */


public byte[] encryptData(byte[] byteDataToEncrypt, Key secretKey, String Algorithm) {
    byte[] byteCipherText = new byte[200];

    try {
    Cipher aesCipher = Cipher.getInstance(Algorithm);

/**
 *  Step 3. Initialize the Cipher for Encryption 
 */
    if(Algorithm.equals("AES")){
        aesCipher.init(Cipher.ENCRYPT_MODE,secretKey,aesCipher.getParameters());
        }
        else if(Algorithm.equals("RSA/ECB/PKCS1Padding")){
        aesCipher.init(Cipher.ENCRYPT_MODE,secretKey);
        } 

/**
 *  Step 4. Encrypt the Data
 *          1. Declare / Initialize the Data. Here the data is of type String
 *          2. Convert the Input Text to Bytes
 *          3. Encrypt the bytes using doFinal method 
 */
byteCipherText = aesCipher.doFinal(byteDataToEncrypt); 
strCipherText = new BASE64Encoder().encode(byteCipherText);

    }

    catch (NoSuchAlgorithmException noSuchAlgo)
    {
        System.out.println(" No Such Algorithm exists " + noSuchAlgo);
    }

        catch (NoSuchPaddingException noSuchPad)
        {
            System.out.println(" No Such Padding exists " + noSuchPad);
        }

            catch (InvalidKeyException invalidKey)
            {
                System.out.println(" Invalid Key " + invalidKey);
            }

            catch (BadPaddingException badPadding)
            {
                System.out.println(" Bad Padding " + badPadding);
            }

            catch (IllegalBlockSizeException illegalBlockSize)
            {
                System.out.println(" Illegal Block Size " + illegalBlockSize);
                illegalBlockSize.printStackTrace();
            }
            catch (Exception exp)
            {
                exp.printStackTrace();
            }

return byteCipherText;
}
/**
 *  Step 5. Decrypt the Data
 *          1. Initialize the Cipher for Decryption 
 *          2. Decrypt the cipher bytes using doFinal method 
 */

public byte[] decryptData(byte[] byteCipherText, Key secretKey, String Algorithm) {
    byte[] byteDecryptedText = new byte[200];

    try{    
Cipher aesCipher = Cipher.getInstance(Algorithm);
if(Algorithm.equals("AES")){
aesCipher.init(Cipher.DECRYPT_MODE,secretKey,aesCipher.getParameters());
}
else if(Algorithm.equals("RSA/ECB/PKCS1Padding")){
aesCipher.init(Cipher.DECRYPT_MODE,secretKey);
} 

byteDecryptedText = aesCipher.doFinal(byteCipherText);
strDecryptedText = new String(byteDecryptedText);
    }

catch (NoSuchAlgorithmException noSuchAlgo)
{
    System.out.println(" No Such Algorithm exists " + noSuchAlgo);
}

    catch (NoSuchPaddingException noSuchPad)
    {
        System.out.println(" No Such Padding exists " + noSuchPad);
    }

        catch (InvalidKeyException invalidKey)
        {
            System.out.println(" Invalid Key " + invalidKey);
            invalidKey.printStackTrace();
        }

        catch (BadPaddingException badPadding)
        {
            System.out.println(" Bad Padding " + badPadding);
            badPadding.printStackTrace();
        }

        catch (IllegalBlockSizeException illegalBlockSize)
        {
            System.out.println(" Illegal Block Size " + illegalBlockSize);
            illegalBlockSize.printStackTrace();
        }

        catch (InvalidAlgorithmParameterException invalidParam)
        {
            System.out.println(" Invalid Parameter " + invalidParam);
        }

return byteDecryptedText;
}


public static byte[] convertStringToByteArray(String strInput) {
    strInput = strInput.toLowerCase();
    byte[] byteConverted = new byte[(strInput.length() + 1) / 2];
    int j = 0;
    int interimVal;
    int nibble = -1;

    for (int i = 0; i < strInput.length(); ++i) {
        interimVal = strHexVal.indexOf(strInput.charAt(i));
        if (interimVal >= 0) {
            if (nibble < 0) {
                nibble = interimVal;
            } else {
                byteConverted[j++] = (byte) ((nibble << 4) + interimVal);
                nibble = -1;
            }
        }
    }

    if (nibble >= 0) {
        byteConverted[j++] = (byte) (nibble << 4);
    }

    if (j < byteConverted.length) {
        byte[] byteTemp = new byte[j];
        System.arraycopy(byteConverted, 0, byteTemp, 0, j);
        byteConverted = byteTemp;
    }

    return byteConverted;
}

public static String convertByteArrayToString(byte[] block) {
    StringBuffer buf = new StringBuffer();

    for (int i = 0; i < block.length; ++i) {
        buf.append(strHexVal.charAt((block[i] >>> 4) & 0xf));
        buf.append(strHexVal.charAt(block[i] & 0xf));
    }

    return buf.toString();
}
}
