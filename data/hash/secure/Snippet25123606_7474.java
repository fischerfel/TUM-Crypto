import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//import javax.xml.bind.DatatypeConverter;
import android.util.Base64;


public class Support {

    private static final String SALT = "3D5900AE-111A-45BE-96B3-D9E4606CA793";
    private static final int MAX_HASH_ITERATIONS = 10;

    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String result = Support.GetPasswordHash("test");
        System.out.println(result);
    }

    public static String GetPasswordHash(String plaintextPassword) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String hashData = plaintextPassword;
        for (int hashLimit = 0; hashLimit < MAX_HASH_ITERATIONS; hashLimit++) {
            hashData = GetHash(SALT + hashData);
        }
        return hashData;
    }

    //Gets the hash value of the data using SHA512Managed
    private static String GetHash(String unhashedData) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return getMD5Password(unhashedData);
    }

    //Verifies the hash
    public static boolean VerifyHashedPassword(String plaintextPassword, String encryptedPassword) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String hashData = GetPasswordHash(plaintextPassword);
        return encryptedPassword.equals(hashData);
    }


    public static String getMD5Password(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        MessageDigest digest = java.security.MessageDigest.getInstance("SHA-512"); 
        digest.update(password.getBytes("UTF-16LE")); 
        byte messageDigest[] = digest.digest();

        StringBuilder sb = new StringBuilder();
        for(int iPos = 0; iPos < messageDigest.length; iPos++) {
            String h = Integer.toHexString(0xFF & messageDigest[iPos]);
            while (h.length() < 2) {
                h = "0" + h;
            }
            sb.append(h);
        }

        String md5String = sb.toString().toUpperCase();     
        String res = Base64.encodeToString(md5String.getBytes(), Base64.DEFAULT);

        return res;
    }
}
