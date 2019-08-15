import  java.io.*;
import  java.math.BigInteger;
import java.security.NoSuchAlgorithmException;


public class BigIntegerTest {



/**
 * @param args
 * @throws NoSuchAlgorithmException 
 */
public static void main(String[] args) throws NoSuchAlgorithmException {
    // TODO Auto-generated method stub

    byte[] message3;
    BigInteger bigmessage1 = null;
    BigInteger bigmessage2 = null;      
    BigInteger bigmessage3 = null;



    bigmessage1 = BigInteger.ZERO; 

    System.out.println("BigInteger.ZERO: "+bigmessage1);

    System.out.println("null: "+bigmessage2);

    String filename="./bin/test1a";

    message3 = To_Byte_Array(filename);

    bigmessage3= new BigInteger (message3);

    System.out.println("test1a: "+ bigmessage3);

    String comp = null;

    if (bigmessage1==bigmessage3) {comp="true";} else {comp="false";};

    System.out.println("bigmessage1==bigmessage3: " + comp);

    if (BigInteger.ZERO==BigInteger.ZERO) {comp="true";} else {comp="false";};

    System.out.println("BigInteger.ZERO==BigInteger.ZERO: " + comp);

}

public static byte[] To_Byte_Array (String filename) throws java.security.NoSuchAlgorithmException {
      //Liest die Nachricht, die in der Datei filename gespeichert ist, ein und
      //speichert sie als byte-Array in message.

      //lokale Variablen:
      byte[] data = null;
     // MessageDigest hash = MessageDigest.getInstance("SHA-512");//SHA2 //removed

      //Streams, in:
      File textFile;//Textdatei
      FileInputStream in;//Dateieingabe-Stream

      try {

          textFile = new File(filename);
          in = new FileInputStream(textFile);
          int size = (int)textFile.length(); // Dateilaenge
          int read = 0;    // Anzahl der gelesenen Zeichen
          data = new byte[size];      // Lesepuffer
          // Auslesen der Datei
          while (read < size)
            read =+ in.read(data, read, size-read);
          in.close();
          // Schreiben des Lesepuffers in Instanz von MessageDigest und speichern des Hash-Werts in message
          //hash.update (data);//removed
          //message=hash.digest ();//removed


      }//try
      catch (IOException ex) {
        ex.printStackTrace();
      }
      return data;//added
    }//To_Byte_Array


}
