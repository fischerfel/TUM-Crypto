    import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

public static void main(String[] args) {
    try{
        MessageDigest alg = MessageDigest.getInstance("MD5");
        String password = "123456";
        alg.reset();
        alg.update(password.getBytes());
        byte[] msgDigest = alg.digest();

        BigInteger number = new BigInteger(1,msgDigest);

        String str = number.toString(16);
        System.out.println(str);

    }catch(NoSuchAlgorithmException e){
        e.printStackTrace();
    }

}
