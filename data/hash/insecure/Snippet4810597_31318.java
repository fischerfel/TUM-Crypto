import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.UUID;

import javax.xml.bind.DatatypeConverter;
import org.apache.commons.codec.binary.Base64;

public class OminitureWSUtil {

private static MessageDigest SHA1;

static {
    try {
        SHA1 = MessageDigest.getInstance("SHA1");

    } catch(NoSuchAlgorithmException nae) {
        throw new RuntimeException(nae);
    }
}

static class OmniturePasswordDigest {
    private final String timestamp;
    private final String nonce;
    private final String secret;

    private String password;

    public OmniturePasswordDigest(String secret) {
        Calendar c = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT+0"));
        c.setTime(new Date());

        //timestamp =  DatatypeConverter.printDateTime(c);
        //nonce = UUID.randomUUID().toString().replace("-", "");

        timestamp = "2011-01-26T20:10:56Z";
        nonce = "MTkyMTYwZWMzMjIzZGJmYzNiYmE5M2E5";

        this.secret = secret;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getNonce() {
        return nonce;
    }

    public String generatePassword() {
        if(password == null) {
            String beforeEncryption = nonce+timestamp+secret;
            System.out.println("before encryption, encoding: " + beforeEncryption);

            try {
                SHA1.reset();
                byte[] toEncrypt = beforeEncryption.getBytes("UTF-8");
                //SHA1.update(toEncrypt, 0, toEncrypt.length);
                SHA1.update(beforeEncryption.getBytes());
            } catch (UnsupportedEncodingException uee) {
                throw new RuntimeException(uee);
            }

            byte[] encryptedRaw = SHA1.digest();
            byte[] encoded = Base64.encodeBase64(encryptedRaw);

            try {
                password = new String(encoded, "UTF-8");
            } catch (UnsupportedEncodingException uee) {
                throw new RuntimeException(uee);
            }
        }

        return password;
    }
}


public static OmniturePasswordDigest generatePasswordDigest(String secret) {
    return new OmniturePasswordDigest(secret);
}

public static void main(String[] args) {
    OmniturePasswordDigest opd = generatePasswordDigest("1779ab07fb93a01e3d4a6ee174124b91");
    System.out.println("nonce: " + opd.getNonce());
    System.out.println("timestamp: " + opd.getTimestamp());
    System.out.println("password: " + opd.generatePassword());

    if("Lr+m+/6y3XUxvjd8Rtn75gqn/b4=".equals(opd.generatePassword())) {
        System.out.println("all good");
    } else {
        System.out.println("generated password is not the same, it should be: " + 
                "Lr+m+/6y3XUxvjd8Rtn75gqn/b4=");
    }

}
