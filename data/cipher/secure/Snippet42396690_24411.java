import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

class AES256JavaPhp{
        public static void main(String[] args) throws Exception {
                Base64 base64 = new Base64();
                Cipher ciper = Cipher.getInstance("AES/CBC/PKCS5Padding");
                SecretKeySpec key = new
                          SecretKeySpec("PasswordPassword".getBytes("UTF-8"),"AES");
                IvParameterSpec iv = new IvParameterSpec
                      ("dynamic@dynamic@".getBytes("UTF-8"),0,ciper.getBlockSize());
            //Encrypt
                ciper.init(Cipher.ENCRYPT_MODE, key,iv);
                byte[] encryptedCiperBytes = base64.encode
                                              ((ciper.doFinal("Hello".getBytes())));
                System.out.println("Ciper : "+new String(encryptedCiperBytes));
            //Decrypt
                ciper.init(Cipher.DECRYPT_MODE, key,iv);    
                byte[] text = ciper.doFinal(base64.decode(encryptedCiperBytes));
                System.out.println("Decrypt text : "+new String(text));
          }
    }
