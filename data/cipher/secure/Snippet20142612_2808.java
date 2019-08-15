import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import android.os.Environment;

/**
 *
 * @author Test01
 */
public class Decryptor extends RSABase {

    private PrivateKey key;
    private String text;

    public Decryptor(String text) {
        this.text = text;
        try {
            key=readPrivateKeyFromFile(Environment.getExternalStorageDirectory()+"/private.key");
        } catch (IOException ex) {
            Logger.getLogger(Decryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private PublicKey readPublicKeyFromFile(String fileName) throws IOException {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(new File(fileName));
            ois = new ObjectInputStream(fis);

            BigInteger modulus = (BigInteger) ois.readObject();
            BigInteger exponent = (BigInteger) ois.readObject();

            //Get Public Key  
            RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, exponent);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PublicKey publicKey = fact.generatePublic(rsaPublicKeySpec);

            return publicKey;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                ois.close();
                if (fis != null) {
                    fis.close();
                }
            }
        }
        return null;
    }

    private PrivateKey readPrivateKeyFromFile(String fileName) throws IOException {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(new File(fileName));
            ois = new ObjectInputStream(fis);

            BigInteger modulus = (BigInteger) ois.readObject();
            BigInteger exponent = (BigInteger) ois.readObject();

            //Get Private Key  
            RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(modulus, exponent);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            key = fact.generatePrivate(rsaPrivateKeySpec);

            return key;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                ois.close();
                if (fis != null) {
                    fis.close();
                }
            }
        }
        return null;
    }

    public String decrypt(){
        byte[] decrypted=null;
        byte[] decryptable=hexToByte(text);
        String dec=null;
        try {
            Cipher cipher=Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            decrypted=cipher.doFinal(decryptable);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Decryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(Decryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(Decryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(Decryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Decryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        dec = new String(decrypted);
        return dec;
    }
}
