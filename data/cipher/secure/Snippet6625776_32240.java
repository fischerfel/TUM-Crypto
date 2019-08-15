Code snippet:
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

    public class MainClass {

        /**
         * @param args
         * Encryption and Decryption with AES/ECB/PKCS7Padding  and RSA/ECB/PKCS1Padding
         */
        public static void main(String[] args) throws Exception {
            // TODO Auto-generated method stub
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());   
            System.out.println("Enter a Message to be encrypted!");
            // Read an input from console
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isr);
            String s = br.readLine();

            // Get the bytes of the input stream. Convert the input text
            // to bytes.
            byte[] input = s.getBytes("UTF8");

            System.out.println("Input Message : " + new String(input));

            // AES 128 bits Symmetric encryption of data

            // Generate the AES key for Symmetric AES encryption
            KeyGenerator kgenerator = KeyGenerator.getInstance("AES", "BC");
            SecureRandom random = new SecureRandom();
            kgenerator.init(128, random);

            Key aeskey = kgenerator.generateKey();
            byte[] raw = aeskey.getEncoded();
            int sykLength = raw.toString().length();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

            System.out.println("Generated Symmetric Key :" + raw);
            System.out.println("Generated Symmetric Key Length :" + sykLength);
            System.out.println("Generated Key Length in Bytes: " + raw.length);

            // Encrypt the data using AES cipher
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);         
            byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
            int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
            ctLength += cipher.doFinal(cipherText, ctLength);

            System.out.println("Encrypted Message :" + new String(cipherText));
            System.out.println("Encrypted Message Length: " + ctLength);

           // RSA 1024 bits Asymmetric encryption of Symmetric AES key

          // Generate Public and Private Keys (Can also use a certificate for keys)
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "BC");
            kpg.initialize(1024, random);
            KeyPair kpa = kpg.genKeyPair();
            RSAPublicKey pubKey = (RSAPublicKey) kpa.getPublic();
            RSAPrivateKey privKey = (RSAPrivateKey)kpa.getPrivate();    

          // Encrypt the generated Symmetric AES Key using RSA cipher  
            Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");            
            rsaCipher.init(Cipher.ENCRYPT_MODE, pubKey);            
            byte[] rawRSA = raw.toString().getBytes("UTF8");
            byte[] cipherTextRSA = new byte[rsaCipher.getOutputSize(rawRSA.length)];
            int ctLengthRSA = rsaCipher.update(rawRSA, 0, rawRSA.length, cipherTextRSA, 0);
            ctLengthRSA += rsaCipher.doFinal(cipherTextRSA, ctLengthRSA);

            System.out.println("Encrypted Symmetric Key :" + cipherTextRSA);
            System.out.println("Encrypted Symmetric Key Length :" + ctLengthRSA);
            System.out.println("Encrypted Symmetric Key Length in Bytes: " + cipherTextRSA.length);

            // RSA Decryption of Encrypted Symmetric AES key
            rsaCipher.init(Cipher.DECRYPT_MODE, privKey);
            byte[] plainTextRSA = new byte[rsaCipher.getOutputSize(ctLengthRSA)];
            int ptLengthRSA = rsaCipher.update(cipherTextRSA, 0, ctLengthRSA, plainTextRSA, 0);
            ptLengthRSA += rsaCipher.doFinal(plainTextRSA, ptLengthRSA);
            SecretKeySpec DecrypskeySpec = new SecretKeySpec(plainTextRSA, "AES");  

            System.out.println("Decrypted Symmetric Key: " + new String(plainTextRSA));
            System.out.println("Decrypted Symmetric Key Length: " + ptLengthRSA);       
            System.out.println( "Decrypted Symmetric Key Length in Bytes: " + plainTextRSA.length);

                cipher.init(Cipher.DECRYPT_MODE, DecrypskeySpec, cipher.getParameters());
                byte[] plainText = new byte[cipher.getOutputSize(ctLength)];
                int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);
                ptLength += cipher.doFinal(plainText, ptLength);
                System.out.println("Decrypted Message: " + new String(plainText));
                System.out.println("Decrypted Message Length: " + ptLength);
                System.out.println("Decrypted Message Length in Bytes: " + plainText.length);
        }
    }
