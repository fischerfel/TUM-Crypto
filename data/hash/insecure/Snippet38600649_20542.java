import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



public class Test{
    public static void main(String args[]){


        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            String ssmd5 = "sp00";
            String ShipmentID = new String(md.digest(ssmd5.getBytes()), StandardCharsets.UTF_8);
            System.out.println(ShipmentID);

        }catch(NoSuchAlgorithmException e){
            System.out.println("I'm sorry, but MD5 is not a valid message digest algorithm");
        }



    }
}
