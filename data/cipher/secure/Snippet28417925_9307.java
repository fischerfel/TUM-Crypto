package Secondo;
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
 * Alice.
 * 
 * rappresenta il client che si vuole autenticare al Server
 *
 */


public class Alice 
{
 
 public static final int JabberServerPORT = 9999;
 //public static final byte[] JabberServerADDRESS = new byte[]{127,0,0,1};
 
 public  static void main(String[] args) throws Exception {
  
  
  
  
  
  // p è un numero primo in modo che qualsiasi g possa generare un insieme abbastanza grande
  // inoltre perchè Diffie-Helman sia sicuro anche ((p-1)/2) deve essere primo
  
  int bitLength = 512; 
     SecureRandom rnd = new SecureRandom();
     
     //Attributi per DH
     BigInteger p;
     BigInteger g = new BigInteger(bitLength, rnd);
     
  
     BigInteger Sa=new BigInteger(bitLength, rnd);
     do{
      
      p = g.max(Sa).nextProbablePrime();
      
     } while (p.subtract(BigInteger.ONE).shiftRight(1).isProbablePrime(50));
     
     
     
     BigInteger Ta=g.modPow(Sa,p);
     
     
     
     //
     String Alice=MyUtil.leggiString("inserisci il nome");
     String password=MyUtil.leggiString("inserisci la password");
     
   // System.out.println("stringa del BigInteger Ta "+Ta.toString());
     System.out.println("lunghezza bit Ta"+Ta.bitCount()+"lunghezza bit p"+p.bitCount());
     DatiChipertext prima_cifratura= cifraturaAES( chiaveAES(password),Ta.toByteArray());
     
     byte[] A=prima_cifratura.getChipertext();
     byte[] iv=prima_cifratura.getIv();
        Socket socket=null;
        
        
       
       System.out.println(Alice+","+A+","+g+p+","+iv);
       
        
           
        
        try {
         
         
      socket = new Socket("localhost", JabberServerPORT);
      
         
      
           
       
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
     System.out.println("mi sto connettendo al server");
        
        
        
     
       // System.out.println("lunghezza primo messaggio "+primo_messaggio.length());
         // Invio di A Alice g p
       // System.out.println("lunghezza A"+A.length());
       // System.out.println("chiave primo messaggio"+password);
     PrimoMessaggioAlice primo_messaggio= new PrimoMessaggioAlice(Alice,A,g,p,iv);
        out.writeObject(primo_messaggio);
        out.reset();
        
        System.out.println("primo messaggio inviato");
         // Ricezione di Bob e B
         
        PrimoMessaggioBob primo_messaggio_ricevuto = (PrimoMessaggioBob)in.readObject();
        
               
        
        System.out.println("primo messaggio ricevuto");
      
        
       //  Decifro ottenendo Tb e c1
         
       
        String Bob=primo_messaggio_ricevuto.getBob();
        byte[] iv2=primo_messaggio_ricevuto.getIv();
        byte[] B=primo_messaggio_ricevuto.getB();
       
        
        System.out.println("B ricevuto "+B+" iv "+iv2);
        /**Cipher mDecipher = Cipher.getInstance ("AES/CBC/PKCS5Padding");
        mDecipher.init (Cipher.DECRYPT_MODE, chiaveAES(password));
        */
       
        byte[] B_decriptato=decifraturaAES(chiaveAES(password),B,iv2);
       
        byte[] c1= new byte [4];
        byte[] Tb_decriptato= new byte[B_decriptato.length-4];
        for(int i=0;i<B_decriptato.length;i++)
        {
         if(i<(B_decriptato.length-4))
          Tb_decriptato[i]=B_decriptato[i];
         else
          c1[i]=B_decriptato[i];
        }
      
      //Sostituito con Tb dato da bob
     BigInteger Tb=new BigInteger(Tb_decriptato);
     
     //
     
     //Chiave per i metodi successivi
     BigInteger Kt=Tb.modPow(Sa,p);
     
     
     
     byte[] c2=(SecureRandom.getSeed(4));
     
   //creazione della chiave effimera
     
    
        byte[] secondo_messaggio=concat(c1,c2);
        SecretKeySpec ephimeral_key= new SecretKeySpec(Kt.toByteArray(),"AES");
        
        
     //   System.out.println("lunghezza Kt "+Kt.bitLength()+"lunghezza p "+p.bitLength());
        
        
        
        DatiChipertext secondo_messaggio_chipertext=cifraturaAES(ephimeral_key,secondo_messaggio);
        byte[] secondo_messaggio_criptato=secondo_messaggio_chipertext.getChipertext();
        byte[] iv3=secondo_messaggio_chipertext.getIv();
        System.out.println(iv3);
    //   System.out.println("secondo messaggio inviato "+secondo_messaggio_criptato.getBytes()+"ephimeral key "+Kt.toByteArray());
        /**System.out.println("Tb "+Tb.toString()+" Ta "+Ta.toString());
        */
        
        SecondoMessaggioA secondo_messaggio_da_inviare=new SecondoMessaggioA(secondo_messaggio_criptato,iv3);
        /**
         * Invio del secondo messaggi ocriptato
         */
        
         System.out.println("secondo messaggio inviato "+secondo_messaggio_da_inviare.getC1C2()+secondo_messaggio_da_inviare.getIv());
         
        
        out.writeObject(secondo_messaggio_da_inviare);
        
        out.flush();
        /**
         * Ricezione di c2'
         */
        
        System.out.println("secondo messaggio inviato ");
        
        SecondoMessaggioBob secondo_messaggio_ricevuto= (SecondoMessaggioBob)in.readObject();
       
        System.out.println("secondo messaggio ricevuto ");
        System.out.println(secondo_messaggio_ricevuto);
        /**
         * Verifica che c2 sia uguale a c2'
         */
        
       /* Cipher mDecipher2 = Cipher.getInstance ("AES/CBC/PKCS5Padding");
        mDecipher2.init (Cipher.DECRYPT_MODE, chiaveAES(Kt.toByteArray().toString()));
        */
        
       
        
        byte[] iv4=secondo_messaggio_ricevuto.getIv();
        
        byte[] c2_cifrato=secondo_messaggio_ricevuto.getC2();
        byte[] c2_ricevuto=decifraturaAES(ephimeral_key,c2_cifrato,iv4);
        
        if(c2_ricevuto.equals(c2))
         System.out.println("Server Autenticato");
        else
         System.out.println("Server NON Autenticato");
         
         
      }finally {
   System.out.println("********** Chiudo il socket...");
   try {
    socket.close();
   } catch (Exception e) {
    System.out.println("********** Eccezione b" + e);
   }
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
 
 public static byte[] concat(byte[] a, byte[] b) {
     int aLen = a.length;
     int bLen = b.length;
     byte[] c= new byte[aLen+bLen];
     System.arraycopy(a, 0, c, 0, aLen);
     System.arraycopy(b, 0, c, aLen, bLen);
     return c;
  }
}