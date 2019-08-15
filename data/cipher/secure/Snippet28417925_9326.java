package Secondo;


import java.math.BigInteger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;


import javax.xml.bind.DatatypeConverter;






import java.security.MessageDigest;
import java.security.SecureRandom;
import java.io.*;
import java.net.*;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;









import java.security.spec.KeySpec;
import java.net.Socket;
import java.math.BigInteger;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;

import java.security.*;
import java.io.*;
import java.net.*;

import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.*;

import java.security.spec.KeySpec;
import java.net.Socket;
import javax.crypto.spec.IvParameterSpec;

import javax.xml.bind.DatatypeConverter;


/**
 * 
 * @author Bazzotti 
 * 
 * Bob.
 * 
 * rappresenta il Server che deve autenticare il Client
 *
 */


public class Bob 
{
 
 private static String Bob ="Bob";
 public static final int JABBERSERVERPORT = 9999;
 
 public static void main(String[] args) throws Exception {
  
  
  Utenti utenti=new Utenti();
  
  utenti.inizzializzazioneUtenti();
  
  
  ServerSocket ServS = new ServerSocket(JABBERSERVERPORT);
  Socket socket = null;
     
  int bitLength = 256; 
     SecureRandom rnd = new SecureRandom();
     
     try {
   
   socket = ServS.accept();
   
   
   
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
         
            out.flush();
            
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
      /**
       * Ricezione della stringa
       */
   
   System.out.println("sto aspettando la connessione");
   
     PrimoMessaggioAlice primo_messaggio_ricevuto=(PrimoMessaggioAlice)in.readObject() ;
     

     
     
     
      
    //  System.out.println(Alice_A_g_p.length);
      String Alice =primo_messaggio_ricevuto.getAlice();
      
      
      String password =utenti.cerca(Alice);
      
      byte[] iv1=primo_messaggio_ricevuto.getIv();
      
      BigInteger p= primo_messaggio_ricevuto.getP();
      BigInteger g= primo_messaggio_ricevuto.getG();
      
      byte[] A=primo_messaggio_ricevuto.getA();
  //creazione della chiave
      
      SecretKey chiave=chiaveAES (password);
      
        
         System.out.println(" A è "+A+" iv byte "+iv1);
        
  //Decodifica di A
         
         
        // System.out.println("chiave primo messaggio"+password);
         BigInteger Ta=new BigInteger(decifraturaAES(chiave,A,iv1));
         
         
      
  //    byte [] salt =Alice_A_g_p_salt[4].getBytes("UTF-8");
  //Calcolo Tb
      
      byte[] c1=(SecureRandom.getSeed(4));
      
      BigInteger Sb=new BigInteger(bitLength-1, rnd);
      
      BigInteger Tb=g.modPow(Sb,p);
  //Crittografo messaggio
      
      byte[] primo_messaggio_da_crittografare=concat(Tb.toByteArray(),c1);
      
 //     System.out.println("Tb, c1"+primo_messaggio_da_crittografare.getBytes());
     
         
         DatiChipertext primo_chipertext=cifraturaAES(chiaveAES(password),primo_messaggio_da_crittografare);
      byte[] B=primo_chipertext.getChipertext();
      byte[] iv2=primo_chipertext.getIv();
         
      PrimoMessaggioBob Bob_B= new PrimoMessaggioBob(Bob,B,iv2);
      
  //    System.out.println(" B "+B.getBytes());
      //System.out.println("Bob, B byte "+primo_messaggio_inviato);
  //    System.out.println("Bob, B stringa "+Bob_B.getBytes()+"\n iv "+iv);
      
      
      //Chiave per i metodi successivi
      BigInteger Kt=Ta.modPow(Sb,p);
      
      /**
       * Invio di Tb e Bob
       */
      out.writeObject(Bob_B);
         
         out.flush();
         
         System.out.println("primo messaggio inviato");
      /**
       * Ricevo secondo messaggio c1 e c2
       */
      
      SecondoMessaggioA secondo_messaggio_ricevuto=(SecondoMessaggioA)in.readObject();
      
      
      System.out.println("secondo messaggio ricevuto");
     
      
  //Decodifico c1 e c2
      
      
      
      /**
       * Trasformazione in stringa non corretta, oppure dimensione non va bene
       */
      System.out.println("lunghezza Kt "+Kt.bitLength()+"lunghezza p "+p.bitLength());
      SecretKeySpec ephimeral_key= new SecretKeySpec(Kt.toByteArray(),"AES");
               
         
         
         // System.out.println("secondo messaggio ricevuto "+secondo_messaggio_ricevuto_stringa.getBytes()+"ephimeral key "+Kt.toByteArray());
        //  System.out.println("Tb "+Tb.toString()+" Ta "+Ta.toString());
          
         
          
          byte[] iv3=secondo_messaggio_ricevuto.getIv();
          
          System.out.println(iv3);
          
          byte[] secondo_messaggio_da_decodificare=secondo_messaggio_ricevuto.getC1C2();
          byte[] c1_c2=decifraturaAES(ephimeral_key,secondo_messaggio_da_decodificare,iv3);
          
          byte[] c1_ricevuto=new byte[4];
          byte[] c2=new byte[4];
          
          for (int i=0;i<4;i++)
          {
           c1_ricevuto[i]=c1_c2[i];
          }
          for (int i=4;i<9;i++)
          {
           c2[i]=c1_c2[i];
          }
          
         
          if(c1_ricevuto.equals(c1))
          {
           System.out.println("Autenticato il client");
           
           
              DatiChipertext secondo_messaggio_criptato=cifraturaAES(ephimeral_key,c2);
              
              byte[] c2_criptato=secondo_messaggio_criptato.getChipertext();
              byte[] iv4=secondo_messaggio_criptato.getIv();
             
              SecondoMessaggioBob secondo_messaggio_da_inviare=new SecondoMessaggioBob(c2_criptato,iv4);
              /**
               * invio di c2
               */
              
              
              out.writeObject(secondo_messaggio_da_inviare);
              out.flush();
          }
           
          else
           System.out.println("client sbagliato");
          
  
   }finally {
    System.out.println("********** Chiudo il socket...");
    try {
     socket.close();
    } catch (Exception e) {
     System.out.println("********** Eccezione b" + e);
    }
  
  ServS.close();
 }

 }
 private static DatiChipertext cifraturaAES  (SecretKey secret, byte[] messaggio) throws Exception
 {
  
  
        //cifratura
    
        Cipher mEcipher = Cipher.getInstance ("AES/CBC/PKCS5Padding");
       /* byte[] iv = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
   IvParameterSpec ivspec = new IvParameterSpec(iv);
        mEcipher.init (Cipher.ENCRYPT_MODE, secret,ivspec);
        */
        
        mEcipher.init (Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = mEcipher.getParameters();

        byte []iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        System.out.println("iv usato "+iv);
       // byte[] messaggio_da_codificare = DatatypeConverter.parseBase64Binary(messaggio);
       
        //chipertext è ok lungo 80 byte
        
        //String stringa_cifrata=new String (chiphertext);
        
        //Devono essere 80 caratteri o un multiplo
        //System.out.println("lunghezza caratteri stringa messaggio"+stringa_cifrata.length());
  
        
        return new DatiChipertext (messaggio,iv);
        
        
 }
  
 private static SecretKey chiaveAES (String chiave) throws Exception
 {
  //creazione della chiave
     //SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    // byte [] salt={0,0,0,0,0,0,0,0};
    // SecretKeySpec tmp = new SecretKeySpec (chiave.getBytes("UTF-8"),"AES");
   //  SecretKey tmp = factory.generateSecret (spec);
     
  //calcolo sha-256 della chiave
  MessageDigest digest = MessageDigest.getInstance("SHA-256");
  byte[] hash = digest.digest(chiave.getBytes());
       
  SecretKey secret = new SecretKeySpec (hash, "AES");
        return secret;
 }
 
 private static byte[] decifraturaAES (SecretKey chiave, byte[] messaggio, byte[] iv) throws Exception
 {
  
  
     
  
   Cipher mDecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
  
   IvParameterSpec ivspec = new IvParameterSpec(iv);
        mDecipher.init (Cipher.DECRYPT_MODE, chiave,ivspec);
  
        // byte [] messaggio_da_decodificare=DatatypeConverter.parseBase64Binary(messaggio);
       
        // System.out.println("lunghezza byte chiper"+messaggio_da_decodificare.length);
         //byte [] messaggio_da_decodificare=messaggio.getBytes("UTF-8");
         return mDecipher.doFinal(messaggio);
         
 }
 
 public static byte[] concat(byte[] a, byte[] b)
 {
     int aLen = a.length;
     int bLen = b.length;
     byte[] c= new byte[aLen+bLen];
     System.arraycopy(a, 0, c, 0, aLen);
     System.arraycopy(b, 0, c, aLen, bLen);
     return c;
  }
}