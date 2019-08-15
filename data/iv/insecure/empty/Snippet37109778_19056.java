package automation;    
import java.io.IOException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Mainjava {

    public static void main(String[] args) {        
                String cryptoAlg = "AES";
                try{
                    SecretKeySpec keyspec = new SecretKeySpec(new byte[32], cryptoAlg);
                    Cipher c = Cipher.getInstance(cryptoAlg + "/CBC/NoPadding");
                    c.init(Cipher.ENCRYPT_MODE, keyspec, new IvParameterSpec(new byte[16]));
                    System.out.println("Success");
                }
                catch(Exception e){
                    System.err.println("************ The Java Virtual Machine can't handle strong cryptography.\n************ This will lead to problems with some services and subsystems!");
                }
            }   }
