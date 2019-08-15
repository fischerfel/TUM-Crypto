import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.Security;
import java.io.File;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.io.*;
import java.security.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Pgm {
    public static void main(String[] args) {
        try {
            KeyPairGenerator dsa = KeyPairGenerator.getInstance("DSA");
            SecureRandom random = new SecureRandom();
            dsa.initialize(1024, random);
            KeyPair keypair = dsa.generateKeyPair();
            PrivateKey privateKey = (PrivateKey) keypair.getPrivate();
            byte[] key = "�u���1Ù�iw&a".getBytes();
            Key aesKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            String currentDir = System.getProperty("user.dir");
            // encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] abc = privateKey.getEncoded();

            byte[] encrypted = cipher.doFinal(abc);
            // System.out.println("len="+encrypted.length());
            File dir = new File(currentDir);
            File private_file = new File(dir, "privatekey.txt");
            if (!private_file.exists()) {
                private_file.createNewFile();
            }
            FileOutputStream fileos = new FileOutputStream(private_file);
            ObjectOutputStream objectos = new ObjectOutputStream(fileos);
            objectos.writeObject(encrypted);
            objectos.close();
            fileos.close();

            File file_private = new File(dir, "privatekey.txt");
            FileInputStream fileo = new FileInputStream(file_private);
            ObjectInputStream objos = new ObjectInputStream(fileo);
            Object obj = objos.readObject();
            byte[] encrypted1 = (byte[]) obj;
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            String decrypted = new String(cipher.doFinal(encrypted1));
            if (decrypted.equals(new String(abc)))
                System.out.println("true");
            else
                System.out.println("false");
            Signature tosign = Signature.getInstance("DSA");
            byte[] val = decrypted.getBytes();

            KeyFactory generator = KeyFactory.getInstance("DSA");
            EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(val);
            PrivateKey privatekey1 = generator.generatePrivate(privateKeySpec);
            tosign.initSign(privatekey1);

        } catch (Exception e) {
            System.out.println("failed");
            e.printStackTrace();

        }

    }
}
