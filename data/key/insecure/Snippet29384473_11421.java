import org.apache.commons.codec.binary.Base64;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncodeData {

public static void main(String[] args) throws Exception {
    String myAppContext = "abc123def";
    String consumerSecret = "959595";
    String algorithm = "HMACSHA256";
    byte[] encodedContext;

    // Base64 encoded context;
    encodedContext = new Base64(true).encode(myAppContext.getBytes());
    System.out.print("Encoded Context : ");
    System.out.println(encodedContext);

    //Generate Signed context           
    SecretKey hmacKey = new SecretKeySpec(consumerSecret.getBytes(), algorithm);
    Mac mac = Mac.getInstance(algorithm);
    mac.init(hmacKey);

    byte[] digest = mac.doFinal(myAppContext.getBytes());       
    System.out.print("Created digest : ");
    System.out.println(digest);

    // Signed Based64 context and Base64 encoded context        
    String messageToSend = digest.toString() + "." + encodedContext.toString();
    System.out.println(messageToSend);
}   
}
