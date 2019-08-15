import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESDecrypter
{
        Cipher dcipher;

        public AESDecrypter(SecretKey key)
        {

                try
                {
                        dcipher = Cipher.getInstance("AES");
                        dcipher.init(Cipher.DECRYPT_MODE, key);
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }
        }

        byte[] buf = new byte[1024];

        public void decrypt(InputStream in, OutputStream out)
        {
            System.out.println("decrypting");
            try
                {
                        in = new CipherInputStream(in, dcipher);
                        int numRead = 0;
                        while ((numRead = in.read(buf)) >= 0)
                        {
                                out.write(buf, 0, numRead);
                        }
                        out.close();
                }
                catch (java.io.IOException e)
                {
                }
        }

        public static void main(String args[])
        {
                try
                {
                        byte[] keystr ={(byte) 0x12,(byte) 0x34,0x55,(byte) 0x66,0x67,(byte)0x88,(byte)0x90,0x12,(byte) 0x23,0x45,0x67,(byte)0x89,0x12,0x33,(byte) 0x55,0x74};
                        SecretKeySpec sks = new SecretKeySpec(keystr,"AES");                        
                        AESDecrypter encrypter = new AESDecrypter(sks);
                        encrypter.decrypt(new FileInputStream("sqllogenc.log"),new FileOutputStream("sqllogdec.log"));
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }
        }
}
