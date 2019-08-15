import java.util.*;
import javax.crypto.Cipher;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.security.AlgorithmParameters;
import javax.crypto.*;
import javax.crypto.SecretKeyFactory;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Cipher{
    private SecureRandom rand;
    private SecretKeyFactory kFact;
    private Cipher AESCipher;
    private SecretKey key;

    public Cipher(char[] mpw, byte[] salt){
            try{
                    kFact = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                    rand = SecureRandom.getInstance("SHA1PRNG");
                    AESCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    PBEKeySpec spec = new PBEKeySpec(mpw, salt, 1024, 256);
                    key = new SecretKeySpec(kFact.generateSecret(spec).getEncoded(),"AES");
            }catch(Exception e){
                    System.out.println("no such algorithm");
            }
    }
    /*Henc[k,m] will return c such that Hdec[k,HEnc[k,m]] = m
     */
    public ArrayList<byte[]> HEnc(byte[] message) throws Exception{
            ArrayList<byte[]> res = new ArrayList<byte[]>(2);
            AESCipher.init(Cipher.ENCRYPT_MODE ,key);
            AlgorithmParameters params = AESCipher.getParameters();
            byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            byte[] ctxt = AESCipher.doFinal(message);
            res.add(0,iv);
            res.add(1,ctxt);
            return res;
    }
    public byte[] HDec(byte[] iv, byte[] cipher) throws Exception{
            AESCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv) );
            System.out.println("decrypting");
            return AESCipher.doFinal(cipher);
    }
    /*public abstract byte[] HDec(SecretKey k, byte[] cipher);
    */
