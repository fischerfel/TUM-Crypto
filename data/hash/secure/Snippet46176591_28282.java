import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import javax.xml.bind.DatatypeConverter;
...

String input = "1234";        
byte[] hashBytes = MessageDigest.getInstance("SHA-256")
        .digest(input.getBytes(StandardCharsets.UTF_8));
String hash = DatatypeConverter.printHexBinary(hashBytes);
System.out.println(hash);
