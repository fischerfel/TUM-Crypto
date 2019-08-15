import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
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
public class Encryptor extends RSABase {

    private KeyPair pair;
    private String text;
    public Encryptor(String text) {
        this.text=text;
        try {
            KeyPairGenerator kpg=KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            pair=kpg.generateKeyPair();
            KeyFactory fact=KeyFactory.getInstance("RSA");
            RSAPublicKeySpec pub=fact.getKeySpec(pair.getPublic(), RSAPublicKeySpec.class);
            RSAPrivateKeySpec priv=fact.getKeySpec(pair.getPrivate(), RSAPrivateKeySpec.class);
            saveKeyToFile(Environment.getExternalStorageDirectory()+"/public.key", pub.getModulus(), pub.getPublicExponent());
            saveKeyToFile(Environment.getExternalStorageDirectory()+"/private.key", priv.getModulus(), priv.getPrivateExponent());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void saveKeyToFile(String filename, BigInteger mod, BigInteger exp){
        ObjectOutputStream fileOut=null;
        try {
            fileOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));
            fileOut.writeObject(mod);
            fileOut.writeObject(exp);
        } catch (IOException ex) {
            Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                fileOut.close();
            } catch (IOException ex) {
                Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public String encrypt(){
        byte[] encrypted=null;
        try {
            byte[] encryptable=text.getBytes("UTF-8");
            PublicKey key=pair.getPublic();
            Cipher cipher=Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encrypted=cipher.doFinal(encryptable);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Encryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return toHexString(encrypted);
    }
}
