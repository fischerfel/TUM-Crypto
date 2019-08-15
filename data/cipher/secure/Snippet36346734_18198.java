package client;
import java.io.*;
import java.security.*;
import javax.crypto.*;
import java.util.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.crypto.spec.DHParameterSpec;
import java.security.spec.*;


class Client{
    private static PublicKey publicKey = null;
    private static PrivateKey privateKey = null;
    private static PublicKey rsaBobPub = null;
    private static SecretKey SecretSharedKeyCipher = null;
    private static SecretKey SecretSharedKeyIntgSend = null;
    private static SecretKey SecretSharedKeyIntRecv = null; 
    private static KeyAgreement aKeyAgreement = null;


    public static void main(String args[]) throws ClassNotFoundException, `IOException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidParameterSpecException, InvalidKeySpecException{`
        Client client = new Client();
        KeyPairGenerator keyGen;
        byte[] alicePub;
        Cipher cipher2;
        byte[] encryptedDH = null;  
        byte[] bobEncryptedDH = null;
        OutputStream dh;
        InputStream bobDHConn;

            Socket connection = new Socket("localhost", 4129);

            //Generate Keys & then send to Bob
                keyGen = KeyPairGenerator.getInstance("RSA");
                keyGen.initialize(2048);
                KeyPair keyPair = keyGen.genKeyPair();
                publicKey = keyPair.getPublic();
                privateKey = keyPair.getPrivate();

           //Send Public Key to Bob
                ObjectOutputStream toBob = new ObjectOutputStream(connection.getOutputStream());
                toBob.writeObject(publicKey);

           //Receive Bob's Public Key
                ObjectInputStream fromBob;
                fromBob = new ObjectInputStream(connection.getInputStream());
                rsaBobPub = (PublicKey) fromBob.readObject();

  //SET UP DIFFIE HELLMAN PROTOCOL
  //For some reason, when receiving Bob's DH param, I am getting a lot of issues.
            //Exchange DH info
                DHParameterSpec paramSpec;
                AlgorithmParameterGenerator paramGen = AlgorithmParameterGenerator.getInstance("DH");
                paramGen.init(512);
                AlgorithmParameters parameters = paramGen.generateParameters();
                paramSpec = (DHParameterSpec) parameters.getParameterSpec(DHParameterSpec.class);

            //Generate Key Pair
                KeyPairGenerator aliceKpGen = KeyPairGenerator.getInstance("DH");
                aliceKpGen.initialize(paramSpec);
                KeyPair aliceKp = aliceKpGen.generateKeyPair();
                aKeyAgreement = KeyAgreement.getInstance("DH");
                aKeyAgreement.init(aliceKp.getPrivate());
                alicePub = aliceKp.getPublic().getEncoded();
                //System.out.println(aliceKp.getPublic())
                //System.out.println(aliceKp.getPublic().getEncoded())
                //Send Alice's encrypted DH byte info to Bob
               /*     cipher2 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                    cipher2.init(Cipher.ENCRYPT_MODE, rsaBobPub);
                    encryptedDH = cipher2.doFinal(alicePub);
                    System.out.print(encryptedDH);
                */
                    dh = connection.getOutputStream();
                    dh.write(alicePub);

             //Recieve Bob's DH Info
                bobDHConn = connection.getInputStream();
                int length;
                byte[] bobDH = null;


                while((length = bobDHConn.available()) == 0){
                    bobDH = new byte[length];
                    int i = 0;
                    while(i < length){
                        i+= bobDHConn.read(bobDH, i, length - i);
                    }
                } 
          //NOT WORKING
           KeyFactory clientKeyFac = KeyFactory.getInstance("DH");
           X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(bobDH);
           PublicKey bobsDHPubKey = clientKeyFac.generatePublic(x509KeySpec);
           aKeyAgreement.doPhase(bobsDHPubKey, true);


        //Generate AES Secret Keys
        SecretKey aesKeyGen = aKeyAgreement.generateSecret("AES");


    }
}
