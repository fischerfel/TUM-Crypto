import java.security.*;
import java.security.KeyStore.*;
import java.io.*;
import java.security.PublicKey;
import java.security.PrivateKey;
import javax.crypto.Cipher;
import java.nio.charset.*;
import sun.security.provider.*;
import  javax.crypto.*;

public class Code {

/**
 * @param args the command line arguments
 */
    public static void main(String[] args) {

        try {

            /* getting data for keystore */

            File file = new File(System.getProperty("user.home") + File.separatorChar + ".keystore");
            FileInputStream is = new FileInputStream(file);
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());

            /*Information for certificate to be generated */ 
            String password = "abcde";
            String alias = "mykeys";
            String alias1 = "skeys";

            String filepath ="C:\\email.txt";

            /* getting the key*/
            keystore.load(is, password.toCharArray());
            PrivateKey key = (PrivateKey)keystore.getKey(alias, "bemylife".toCharArray());
            //PrivateKey key = cert1.getPrivateKey();
            //PublicKey key1= (PrivateKey)key;

            /* Get certificate of public key */
            java.security.cert.Certificate cert = keystore.getCertificate(alias); 

            /* Here it prints the public key*/
            System.out.println("Public Key:");
            System.out.println(cert.getPublicKey());

            /* Here it prints the private key*/
            System.out.println("\nPrivate Key:");
            System.out.println(key);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE,cert.getPublicKey());

            String cleartextFile = "C:\\email.txt";
            String ciphertextFile = "D:\\ciphertextRSA.png";

            FileInputStream fis = new FileInputStream(cleartextFile);
            FileOutputStream fos = new FileOutputStream(ciphertextFile);
            CipherOutputStream cos = new CipherOutputStream(fos, cipher);

            byte[] block = new byte[32];
            int i;
            while ((i = fis.read(block)) != -1) {
                cos.write(block, 0, i);
            }
            cos.close();


            /* computing the signature*/
            Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
            dsa.initSign(key);
            FileInputStream f = new FileInputStream(ciphertextFile);
            BufferedInputStream in = new BufferedInputStream(f);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) >= 0) {
               dsa.update(buffer, 0, len);
           };
           in.close();

           /* Here it prints the signature*/
           System.out.println("Digital Signature :");
           System.out.println( dsa.sign());

           /* Now Exporting Certificate */
           System.out.println("Exporting Certificate. ");
           byte[] buffer_out = cert.getEncoded();
           FileOutputStream os = new FileOutputStream(new File("d:\\signedcetificate.cer"));
           os.write(buffer_out);
           os.close();

           /* writing signature to output.dat file */
           byte[] buffer_out1 = dsa.sign();
           FileOutputStream os1 = new FileOutputStream(new File("d:\\output.dat"));
           os1.write(buffer_out1);
           os1.close();

       } catch (Exception e) {System.out.println(e);}

   }
}
