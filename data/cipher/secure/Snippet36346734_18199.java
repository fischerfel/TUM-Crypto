package server;
import java.io.*;
import java.security.*;
import javax.crypto.*;
import java.util.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

public class Server{
    private static int port = 4129;
    private static PublicKey publicKey = null;
    private static PrivateKey privateKey = null;
    private static PublicKey rsaAlicePub = null;
    private static ServerSocket server = null;
    private static SecretKey SecretSharedKeyCipher = null;
    private static SecretKey SecretSharedKeyIntgSend = null;
    private static SecretKey SecretSharedKeyIntRecv = null;

    public static void main(String args[]) throws ClassNotFoundException, NoSuchAlgorithmException, IOException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException{
        //Declarations
            Server serv = new Server();
            server = new ServerSocket(4129);
            server.setReuseAddress(true);
            KeyPairGenerator keyGen;
            byte[] cipherText = null;
            InputStream input = null;
            byte[] data = null;
            byte[] decryptedDH;
            InputStream DH = null;
            byte[] DHinfo = null;
            int length;
            byte[] aliceEncryptedDH = null;    
            SecretKey keyGenDH= null;
            InputStream aliceDH = null;
            Cipher cipher;
            PublicKey bobDHPub = null;
            OutputStream sendDH;

            //String message = "bbbbbbbbbbbbbb";
            String message = "bbbbbbb";
            Socket client = server.accept();

            //Get Public Key froM Alice
                ObjectInputStream alicePK;
                alicePK = new ObjectInputStream(client.getInputStream());
                rsaAlicePub = (PublicKey)alicePK.readObject();

            //Generate Bob's keys
                keyGen = KeyPairGenerator.getInstance("RSA");
                keyGen.initialize(2048);
                KeyPair keyPair = keyGen.genKeyPair();
                privateKey = keyPair.getPrivate();
                publicKey = keyPair.getPublic();

            //Send Bob's public Key to Alice
                ObjectOutputStream bobPK;
                bobPK = new ObjectOutputStream(client.getOutputStream());
                bobPK.writeObject(publicKey);

            //Exchange information for DH
            //Decrypt received information using Bob PK
            //You can assume that Bob selects the public parameters of Diffie‐Hellman protocol, and send them to Alice

            DH = client.getInputStream();
            while((length = DH.available()) == 0);
            int i = 0;
            DHinfo = new byte[length];
            while (i < length) {
                i += DH.read(DHinfo, i, length - i);
            }
/*
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            decryptedDH = cipher.doFinal(DHinfo);
           */ 
            KeyFactory clientKeyFac = KeyFactory.getInstance("DH");
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(DHinfo);
            bobDHPub = clientKeyFac.generatePublic(x509KeySpec);

            DHParameterSpec dhParamSpec = ((DHPublicKey) bobDHPub).getParams();

            //Create Bob DH Keys
                KeyPairGenerator bobKpGen = KeyPairGenerator.getInstance("DH");
                bobKpGen.initialize(dhParamSpec);
                KeyPair bobsKeys = bobKpGen.generateKeyPair();

                KeyAgreement bobKeyAgreement = KeyAgreement.getInstance("DH");
                bobKeyAgreement.init(bobsKeys.getPrivate());
                bobKeyAgreement.doPhase(bobDHPub, true);

            //Send Bob's DH Parameters to Alice
            //send bobsKeys.getPublic().getEncoded()
                    sendDH = client.getOutputStream();
                    sendDH.write(bobsKeys.getPublic().getEncoded());

            //Encrypt message.getBytes();

    }

    private void Server() throws IOException{
        server = new ServerSocket(port);


    }



}
