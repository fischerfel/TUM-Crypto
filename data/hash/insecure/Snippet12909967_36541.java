package poc;

import com.sun.xml.internal.ws.message.ByteArrayAttachment;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;


public class HashedPassword {
    public static final String CRYPTOGRAPHY_ALGORITHM = "MD5";
    public static final String CHAR_SET = "UTF8";
    public static void main(String[] arg){
        System.out.println(createPassword("r14@17*$"));
    }
    public static byte[] createPassword(String password){
        byte[] salt = new byte[12];
        byte[] digestedPassword =null;
        byte[] digestedPasswordPwd =null;
        try {
                SecureRandom random = new SecureRandom();
                random.nextBytes(salt);
                MessageDigest mdPassword = MessageDigest.getInstance(CRYPTOGRAPHY_ALGORITHM);
                MessageDigest mdPasswordPawd = MessageDigest.getInstance(CRYPTOGRAPHY_ALGORITHM);

                mdPassword.update(salt);
                mdPassword.update(password.getBytes(CHAR_SET));

                mdPasswordPawd.update(password.getBytes(CHAR_SET));
                digestedPassword = mdPassword.digest();
                digestedPasswordPwd = mdPasswordPawd.digest();
                byte[] resultBytes= new byte[1000];

                System.arraycopy(digestedPassword, 11, resultBytes,0,digestedPassword.length);

                if(Arrays.equals(resultBytes, digestedPasswordPwd)){
                    System.out.println("match");
                }else{
                    System.out.println("no-match");
                }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("digestedPassword : "+digestedPassword);
        System.out.println("digestedPasswordPwd : "+digestedPasswordPwd);
        return digestedPassword;
    }

}
