import java.security.*;
import javax.crypto.spec.*;

String passphrase = "correct horse battery staple";
MessageDigest digest = MessageDigest.getInstance("SHA");
digest.update(passphrase.getBytes());
SecretKeySpec key = new SecretKeySpec(digest.digest(), 0, 16, "AES");
