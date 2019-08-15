import java.security.MessageDigest;

public class PasswordToken {

    static public String makeDigest(String password) 
    {
         String hexStr = "";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.reset();
            byte[] buffer = password.getBytes();
            md.update(buffer);
            byte[] digest = md.digest();


            for (int i = 0; i < digest.length; i++) {
                hexStr +=  Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1 );

        } 
        } catch(Exception e) // If the algo is not working for some reason on this device
                             // we have to use the strings hash code, which 
                             // could allow duplicates but at least allows tokens
        {
            hexStr = Integer.toHexString(password.hashCode()); 
        }

        return hexStr;
    }

    static public boolean validate(String password, String token) 
    {
         String digestToken = "";
         String simpleToken = "";

         digestToken = makeDigest(password);

         if (0 == digestToken.compareTo(token)) 
             return true; 

         if (0 == simpleToken.compareTo(token))
             return true;

         return false; 
    }
}
