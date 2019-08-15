import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;

import sun.misc.BASE64Encoder;

public class GenerateRSAKeys{


    private Key pubKey;
    private Key privKey;

    public static void main(String[] args)
    {
        String input = "C:\\Users\\toto\\Desktop\\nomFichier_entrant.ext";
        String output = "C:\\Users\\toto\\Desktop\\nomFichier_entrant.ext.enc";
        String dec = "C:\\Users\\toto\\Desktop\\nomFichier_entrant.ext.dec";
        String publicKeyFilename = "C:\\Users\\toto\\Desktop\\HR_pubkey_prd.pem";
        String privateKeyFilename = "C:\\Users\\toto\\Desktop\\PE_privkey_prd.pem";

        GenerateRSAKeys generateRSAKeys = new GenerateRSAKeys();

        /*         if (args.length < 2)
        {
            System.err.println("Usage: java "+ generateRSAKeys.getClass().getName()+
            " Public_Key_Filename Private_Key_Filename");
            System.exit(1);
        }

        publicKeyFilename = args[0].trim();
        privateKeyFilename = args[1].trim(); */
        generateRSAKeys.generate(publicKeyFilename, privateKeyFilename);

        //generateRSAKeys.encrypt(input, output);
        generateRSAKeys.encrypt(input, output);
        generateRSAKeys.decrypt(output, dec);

    }

    private void generate (String publicKeyFilename, String privateFilename){

        try {

            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

            // Create the public and private keys
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
            BASE64Encoder b64 = new BASE64Encoder();

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            /* SecureRandom random = new SecureRandom();

            keyGen.initialize(4096, random);

            KeyPair pair = keyGen.generateKeyPair();
            pubKey = pair.getPublic();
            privKey = pair.getPrivate(); */

            SecureRandom random = createFixedRandom();
            generator.initialize(4096, random);

            KeyPair pair = generator.generateKeyPair();
            pubKey = pair.getPublic();
            privKey = pair.getPrivate();

            System.out.println("publicKey : " + b64.encode(pubKey.getEncoded()));
            System.out.println("privateKey : " + b64.encode(privKey.getEncoded()));

            BufferedWriter out = new BufferedWriter(new FileWriter(publicKeyFilename));
            out.write(b64.encode(pubKey.getEncoded()));
            out.close();

            out = new BufferedWriter(new FileWriter(privateFilename));
            out.write(b64.encode(privKey.getEncoded()));
            out.close();


        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public static SecureRandom createFixedRandom()
    {
        return new FixedRand();
    }

    private static class FixedRand extends SecureRandom {

        MessageDigest sha;
        byte[] state;

        FixedRand() {
            try
            {
                this.sha = MessageDigest.getInstance("SHA-1");
                this.state = sha.digest();
            }
            catch (NoSuchAlgorithmException e)
            {
                throw new RuntimeException("can't find SHA-1!");
            }
        }

        public void nextBytes(byte[] bytes){

            int    off = 0;

            sha.update(state);

            while (off < bytes.length)
            {                
                state = sha.digest();

                if (bytes.length - off > state.length)
                {
                    System.arraycopy(state, 0, bytes, off, state.length);
                }
                else
                {
                    System.arraycopy(state, 0, bytes, off, bytes.length - off);
                }

                off += state.length;

                sha.update(state);
            }
        }
    }

    public void encrypt(String input, String output) {
        File outputFile;
        FileInputStream inputStream;
        FileOutputStream outputStream;
        Cipher cipher;
        byte[] inputBytes;
        byte[] outputBytes;

        try {
            outputFile = new File(output);
            inputStream = new FileInputStream(input);
            outputStream = new FileOutputStream(outputFile);
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            inputBytes = new byte[(int) input.length()];
            inputStream.read(inputBytes);
            outputBytes = cipher.doFinal(inputBytes);
            outputStream.write(outputBytes);

            // System.out.println(new String(inputBytes, "UTF-8"));

            System.out.println("encrypt");
            System.out.println(new String(outputBytes, "UTF-8"));
            System.out.println("fin encrypt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void decrypt(String input, String output) {
        File outputFile;
        FileInputStream inputStream;
        FileOutputStream outputStream;
        Cipher cipher;
        byte[] inputBytes;
        byte[] outputBytes;

        try {
            outputFile = new File(output);
            inputStream = new FileInputStream(input);
            outputStream = new FileOutputStream(outputFile);
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            inputBytes = new byte[(int) input.length()];
            inputStream.read(inputBytes);
            outputBytes = cipher.doFinal(inputBytes);
            outputStream.write(outputBytes);

            // System.out.println(new String(inputBytes, "UTF-8"));

            System.out.println("decrypt");
            System.out.println(new String(outputBytes, "UTF-8"));
            System.out.println("fin decrypt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
