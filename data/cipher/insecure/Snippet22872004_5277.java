import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
public class Crypto
{
    public Crypto(){

    }

    public static void main(String args[]){
        Crypto crypto = new Crypto();
        byte encrypted[] = crypto.encrypt("test encryption");
        System.out.println(encrypted);
        String decrypted = crypto.decrypt(encrypted);
        System.out.println(decrypted);

    }

    public byte[] encrypt(String input){
        try{
            Crypto crypto = new Crypto();
            SecretKeySpec key = crypto.hashPhrase();
            Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
            aes.init(Cipher.ENCRYPT_MODE, key);
            return aes.doFinal(input.getBytes());
        }
        catch(Exception e){
            return null;
        }
    }

    public SecretKeySpec hashPhrase(){
        try{
            String code = "some code";
            MessageDigest digest = MessageDigest.getInstance("SHA");
            digest.update(code.getBytes());
            return new SecretKeySpec(digest.digest(), 0, 16, "AES");
        }
        catch(Exception e){
            return null;
        }
    }

    public String decrypt(byte[] input){
        try{
            Crypto crypto = new Crypto();
            SecretKeySpec key = crypto.hashPhrase();
            Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
            aes.init(Cipher.DECRYPT_MODE, key);
            return new String(aes.doFinal(input));
        }
        catch(Exception e){
            return null;
        }
    }
}
