import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import org.bouncycastle.util.encoders.Base64;
import sun.misc.BASE64Decoder;

public class encrypt {

    public static void main(String[] args) {

        encrypt obj = new encrypt();

        obj.process();
    }

    public void process() {


        try {

            String initiatorpassword = "giddibon!";    


            String os = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqLcFdVcV7HdEOotsNLoMPhD74CX1ejzcgfNuiJNy9pTySxbszRBCWxmok3Unul4rX/zyVD/6vDb9nbqRywZIgR46UOn+tR3vGXXPX6igxgS6DYTaQV8W858yOGLuoYwRi5xeQJfczAMU4o+sCxlBbMCqYs4nzW81fi8iF2OEUdrfJcbamhSnksdgfD/nomWy9MESAz1QufrOBnaRX2N0CKsi8SNmzsghpfP15VLiIVV8YXPFKtd9sY37FpY28OKGjKG5wdije/bzFL8qEcPDhqYGuVaGkhX1bkI0iH+UcFtYYrZv/Fyb5jRHXmNLiq4mMG0fMH8ENxNACFtRZTDIIQIDAQAB";


            PublicKey publickeyos = rpos(os);


            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");


            cipher.init(Cipher.ENCRYPT_MODE, publickeyos);

             //error on this line
            System.out.println("\n"+ "\n" +"encryptionresult" + "\n" + "\n" + Base64.toBase64String(cipher.doFinal(initiatorpassword.getBytes())) + "\n");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public PublicKey rpos(String file) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {


        byte[] keyBytes = file.getBytes();

        String pubKey = new String(keyBytes);

        BASE64Decoder decoder = new BASE64Decoder();

        keyBytes = decoder.decodeBuffer(pubKey);

        // generate public key
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        PublicKey publicKey = keyFactory.generatePublic(spec);        

        return publicKey;

    }
}
