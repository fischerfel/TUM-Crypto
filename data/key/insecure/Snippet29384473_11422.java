import org.apache.commons.codec.binary.Base64;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

public class DecodeData {

public static void main(String[] args) throws Exception {
    byte[] myAppContext;
    String consumerSecret = "959595";
    String algorithm = "HMACSHA256";
    String messageRecieved = args[0];
    byte[] singedDecodedContext;

    String recievedDigest = messageRecieved.split("[.]", 2)[0];             
    String encodedContext = messageRecieved.split("[.]", 2)[1];
    myAppContext = new Base64(true).decode(encodedContext);
    System.out.print("Decrypted message : ");
    System.out.println(myAppContext);

    //Check if the message is sent by the correct sender by signing the context and matching with signed context
    SecretKey hmacKey = new SecretKeySpec(consumerSecret.getBytes(), algorithm);
    Mac mac = Mac.getInstance(algorithm);
    mac.init(hmacKey);      
    byte[] digest = mac.doFinal(myAppContext);

    System.out.print("Created digest : ");
    System.out.println(digest);

    if (Arrays.equals(digest, recievedDigest.getBytes())) {
        System.out.println("Message was not tempered and was sent by the correct sender");
    } else {
        System.out.println("Message was tempered or was not sent by the corrrect sender");
    }   
}   
}
