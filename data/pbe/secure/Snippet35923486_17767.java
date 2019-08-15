import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.xml.bind.DatatypeConverter;

public class GarbledStringFactory {

    private String algorithm;

    public GarbledStringFactory(String algorithm){
        this.algorithm = algorithm;
    }
    public String getGarbledString(String string, String salt,  int iterations,  int derivedKeyLength) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory f = SecretKeyFactory.getInstance(this.algorithm);
        KeySpec spec = new PBEKeySpec(string.toCharArray(), salt.getBytes(), iterations, derivedKeyLength * 8);
        SecretKey key = f.generateSecret(spec);
        String hexStr = DatatypeConverter.printHexBinary(key.getEncoded());
        return hexStr;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // TODO Auto-generated method stub
        GarbledStringFactory factory = new GarbledStringFactory("PBKDF2WithHmacSHA256");
        String hash = factory.getGarbledString("1000000000","salt",100000,32);
        System.out.println(hash);
    }
}
