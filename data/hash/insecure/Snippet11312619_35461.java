package test;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.UnsupportedEncodingException;

import java.math.BigInteger;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import java.util.logging.Level;
import java.util.logging.Logger;

/* this method converts the string into hash using SHA1 encryption algorithm */


public class SignatureTest {

    static byte[] sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        System.out.println("sha1 hashed data" + result);
        return result;

    }

    public static void main(String[] args) throws InvalidKeySpecException {
        try {

            String dataString = "70:F3:95:11:14:B3" + "AIG_OCR" + "5";
            byte[] data = sha1(dataString);
            System.out.println(dataString);
            System.out.println("data::::::::::::" + data);
            String data2 = Base64.encode(data);
            System.out.println(data2);

            /* here goes ur signed data  */


            byte[] signedData =
                Base64.decode("Q2BD1bPUKcs/iWpsBlvFtcJfrnzofucHIhc+m3qvKibLO13Z7FLSUQyOUUL9oh5KKpxiQY6b4mcLKYP2N9UPNZlCB6WBowJyDTEGajKNNyBgnn6nm7I/7X+eboQkEEqmSsGC0WA1XFXE+QQD6EXWzEkXzPornC3RnNJoR/fEIc8=");
            System.out.println(" lenghtof signedData:::::" +
                               signedData.length);


            /* here is the modulus reqired to generate the public key*/


            byte[] modulusBytes =
                Base64.decode("pggfRN033/0G9HA5/x5v257h5Zdb13Lown8MTYDVF4ItZRuA6mC148omMvAdOO6wdaak/4atCvrIFrsNlFK8NSY8eb7WKN8WCdamXvHQ6G7FUDMOWs0WNokbhJRabUey/cXtG5b7ODTSr1VX2lV9uEfZEruw5YhmLXof39uftWc=");

            /* here is the exponentBytes reqired to generate the public key*/


            byte[] exponentBytes = Base64.decode("AQAB");
            System.out.println(" lenghtof modulusBytes:::::" +
                               modulusBytes.length);
            BigInteger modulus = new BigInteger(1, modulusBytes);
            BigInteger exponent = new BigInteger(1, exponentBytes);
            RSAPublicKeySpec rsaPubKey =
                new RSAPublicKeySpec(modulus, exponent);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PublicKey rsaPublicKey = fact.generatePublic(rsaPubKey);
            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initVerify(rsaPublicKey); // initiate the signature with public key
            sig.update(data); // update signature with the data that was signed by the card
            Boolean flag =
                sig.verify(signedData); // Test card signature - this always returns false
            System.out.println("flag:::::::::::::" + flag);
        } catch (SignatureException ex) {
            Logger.getLogger(SignatureTest.class.getName()).log(Level.SEVERE,
                                                                null, ex);
        } catch (InvalidKeyException ex) {

            Logger.getLogger(SignatureTest.class.getName()).log(Level.SEVERE,
                                                                null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SignatureTest.class.getName()).log(Level.SEVERE,
                                                                null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SignatureTest.class.getName()).log(Level.SEVERE,
                                                                null, ex);
        }
    }
}
