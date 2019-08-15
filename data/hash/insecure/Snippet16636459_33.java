package myPackage;


import java.io.*;
import java.security.*;
import java.security.cert.*;

import javax.crypto.*;

import org.bouncycastle.openssl.*;
import org.bouncycastle.util.encoders.*;

//import android.os.*;

public class UserSMSVerifier {

    static String signedMail;

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }


    public static String messageGenarator(String origninalMessage) throws Exception{

        //load privateKey, Certificate
        PEMReader userPrivateKey = new PEMReader(
                new InputStreamReader(
                   new FileInputStream("C://Users//Lara//workspace_ee//TestCA_server//WebContent//"+"/pkcs10priv.key")));

        PEMReader userCerti = new PEMReader(
                  new InputStreamReader(
                     new FileInputStream("C://Users//Lara//workspace_ee//TestCA_server//WebContent//"+"/userCert.cer")));


        KeyPair userPrivate = (KeyPair)userPrivateKey.readObject();
        X509Certificate userCert = (X509Certificate)userCerti.readObject();

        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
        //MessageDigest.
        //java.security.MessageDigest
        byte[] dataTosend = origninalMessage.getBytes();

        //generate a SecretKey for Symmetric Encryption
        SymmetricEncrypt encryptUtil = new SymmetricEncrypt();
        SecretKey senderSecretKey = SymmetricEncrypt.getSecret();

        //encrypt the data using a Symmetric Key
        byte[] byteCipherText = encryptUtil.encryptData(dataTosend, senderSecretKey, "AES");
        String strCipherText = new BASE64Encoder().encode(byteCipherText);



        //get reciever's public key
        PublicKey pubKeyReceiver = userCert.getPublicKey();
        //encrypt the SecretKey with the Receivers public key
        byte[] byteEncryptWithPublicKey = encryptUtil.encryptData(senderSecretKey.getEncoded(), pubKeyReceiver,"RSA/ECB/PKCS1Padding");
        String strSenbyteEncryptWithPublicKey = new BASE64Encoder().encode(byteEncryptWithPublicKey);


        md.update(dataTosend);
        byte bytedataTosend[] = md.digest();

        String stringDataTosend = new String();
        for (int i=0; i < bytedataTosend.length;i++){
            stringDataTosend = stringDataTosend + Integer.toHexString((int)bytedataTosend[i] & 0xFF);       }


        //Message to be Signed = Encrypted Secret Key + data
        String strMsgToSign = strSenbyteEncryptWithPublicKey + "|" + stringDataTosend;
        //sign the Messsage
        //char[] password = "password".toCharArray();
        Signature yourSign = Signature.getInstance("MD5withRSA");
        yourSign.initSign(userPrivate.getPrivate());
        yourSign.update(stringDataTosend.getBytes());
        byte[] byteSignedData = yourSign.sign();
        //yourSign.


        //heoolo
        //return new String(Hex.encode(byteSignedData));

        //values transmitted through unsecure channels ==> byteSignedData, strMsgToSign
        String strRecvSignedData = new String (byteSignedData);
        String[] strRecvSignedDataArray = strMsgToSign.split("|");
        int intindexofsep = strMsgToSign.indexOf("|");
        String strEncryptWithPublicKey=strMsgToSign.substring(0, intindexofsep);
        String strHashOfData = strMsgToSign.substring(intindexofsep+1);

        //decrypt to get the symmetric key
        byte[] bytestrEncryptWithPublicKey = new BASE64Decoder().decodeBuffer(strEncryptWithPublicKey);
        byte[] byteDecryptWithPrivateKey = encryptUtil.decryptData(byteEncryptWithPublicKey , userPrivate.getPrivate(), "RSA/ECB/PKCS1Padding");

        //decrypt the data using the Symmetric key
        javax.crypto.spec.SecretKeySpec secretKeySpecDecrypted = new javax.crypto.spec.SecretKeySpec(byteDecryptWithPrivateKey, "AES");
        byte[] byteDecryptText = encryptUtil.decryptData(byteCipherText, secretKeySpecDecrypted, "AES");
        String strDecryptedText = new String(byteDecryptText);
        System.out.println("Decrypted Data is : " + strDecryptedText);

        return new String(Hex.encode(byteSignedData));

    }


}
