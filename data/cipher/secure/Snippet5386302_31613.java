import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.Buffer;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

public class importarLlaves {

 /**
  * @param args
  */
 public static void main(String[] args) {
  File archivo = new File("C:/llaves/Publica 2.txt");
  File archivo2 = new File("C:/llaves/Privada 2.txt");

  try {

   BufferedReader lector = new BufferedReader(new FileReader(archivo));
   String[] linea = lector.readLine().split(":");
   String moduloPublico = linea[1];
   linea = lector.readLine().split(":");
   String exponentePublico = linea[1]; 
   lector.close();

   lector = new BufferedReader(new FileReader(archivo2));
   lector.readLine();
   linea = lector.readLine().split(":");
   String exponentePrivado = linea[1];
   lector.close();

   String algorithm = "RSA";
   KeyFactory factory = KeyFactory.getInstance(algorithm);
   RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(moduloPublico), new BigInteger(exponentePublico));
   PublicKey llavePublica = factory.generatePublic(publicKeySpec);

   RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(new BigInteger(moduloPublico), new BigInteger(exponentePrivado));
   PrivateKey llavePrivada = factory.generatePrivate(privateKeySpec);

   System.out.println(llavePublica.toString());

   System.out.println(llavePrivada.toString());

   //  Iniciar  un objeto para  la  encr / desencr
   Cipher desCipher = Cipher.getInstance("RSA");

   //  Leer, escribir   y encriptar un dato
   BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
   String pwd = stdIn.readLine();
   byte [ ] cleartext = pwd.getBytes();
   String s1 = new String (cleartext);   
   System.out.println ("password original:" + s1 );desCipher.init (Cipher.ENCRYPT_MODE, llavePublica); 
   byte [ ] ciphertext = desCipher.doFinal(cleartext);
   String s2 = new String (ciphertext); 
   System.out.println ("password encriptado:" + s2 );

   //  Ahora desencriptar



   desCipher.init(Cipher.DECRYPT_MODE, llavePrivada);
   byte [ ] cleartext1 = desCipher.doFinal(ciphertext);
   String s3 = new String (cleartext1);
   System.out.println("password desencriptado:"+ s3 );



  } catch (Exception e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }

 }

}
