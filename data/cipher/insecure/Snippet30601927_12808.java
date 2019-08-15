 package rc4_crc32;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.zip.CRC32;
public class RC4_CRC32 {
    public static void main(String[] args) throws Exception{ 
        byte[] key, ciphertext;
        CRC32 c = new CRC32();
        javax.crypto.Cipher r;
                 r = Cipher.getInstance("RC4");
                 key = "1@m@L33tH@x0r!".getBytes("ASCII");
                 SecretKeySpec rc4Key = new SecretKeySpec(key, "RC4");
                 r.init(Cipher.ENCRYPT_MODE, rc4Key); 
                 ciphertext = r.update("Secret!".getBytes("ASCII"));                    
                 c.update(ciphertext);                 
                 System.out.println("Ciphertext = " + ciphertext + ", and CRC = " + c.getValue());     
                 ciphertext[0] = (byte)0x2c;
                 c.update(ciphertext);
                 System.out.println("Now ciphertext = " + ciphertext + ", and CRC = " + c.getValue());
                 c.update(ciphertext);
                 System.out.println("Now ciphertext = " + ciphertext + ", and CRC = " + c.getValue());
                 c.update(ciphertext);
                 System.out.println("Now ciphertext = " + ciphertext + ", and CRC = " + c.getValue());
                 c.update(ciphertext);
                 System.out.println("Now ciphertext = " + ciphertext + ", and CRC = " + c.getValue());
    }    
}
