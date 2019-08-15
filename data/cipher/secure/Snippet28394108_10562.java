import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import android.util.Base64;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.BadPaddingException;
import javax.crypto.ShortBufferException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

public class test
{
    public static void main (String args[]) throws FileNotFoundException, FileNotFoundException,
            UnsupportedEncodingException, IOException, NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
            ShortBufferException, NoSuchProviderException, InvalidAlgorithmParameterException {

        Cipher CheckCipher = Cipher.getInstance("RSA");
        KeyFactory fact = KeyFactory.getInstance("RSA");
// one Block has 320 Bytes encrypted data or up to 309 Bytes decrypted data
        byte[] encryptedBuffer = new byte[320];
// public key
        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger("348349717397217997016459044429876445798461822630613593827475516398929980499253048018915109820386668925158598090923917424889721436728681941788735398708226655486687058044818981687475046995966049152458371358433593409946449307673641432434645409125209056679079565878576322466469782078766479519331465759437687570449909733209874455595086928288016285095448723528960074392901021635076277130066403766286870175583368291319265676733584876043054873738027872260931903992491855282040202055048433338277681906526276056353076551640420301024861314931536846223479281788109044598118149113100016473709709779349227018363507068247312704031804719673776595496482605126340630418742698424978233098157782472641000027452801691972881854034867234891280067908910242015420737297077922221753325197819914227"), new BigInteger("65537"));
        RSAPublicKey publicKey = (RSAPublicKey) fact.generatePublic(pubKeySpec);
// private key
        byte[] encoded = Base64.decode("MIIF3gIBADANBgkqhkiG9w0BAQEFAASCBcgwggXEAgEAAoIBQQDNz13heSUvu7J4d+d1VJBkx6D31ZBcaF3AAA0AFYPPkkPfGF9FneTITk/ft7n1PCT9MzBRzCIFyPEkk93I4tLdP4POUDCRGC1uDBOKBkjo8hMYROt+DtK6vAafhvs/sVJVcMAxYEOZl9wG/SS9KEFU9NktM2pBfq5x747AfjVAJzZuQkL7Mfqejnby7CNgkSk0FFS4top/6EQoe+NeQj1zcDCQQ4/ze5sahc0CF7aaPsq1+aI17CCBh2PFUOvG9/YM5Hj7An2cDURChHr0OJjT8/QwqR49cSSS5P4yhaVxOQqszUAinV+r5FtLGo7iXSMnE/XYV0vpZqJHAD9cPyymcl2N7v1dKWBTmamEp7GsM6T7YnDtSJIeYt9cTi+0IFYMPAe+W93zgH2g/r+nmL/eZapgUD5Gi2ky1bHe0vwn8wIDAQABAoIBQBJ7JYYCt+kiZLNWqQ0rK9Aw8O5wWgdCQ/Di0EgKpox0KO4WpS9+LzYheiCvwd4YqYWnHBasSv3T/nt3X7oTTDYb5v0WzFWrIyE3qmWBjPiGDFrojXEDab7k9X9LwrIEfU0407lbWJapQVPLVYo63CsB1aKudEMvWgDXLy1v59475zJdduBRq5yjVGEZ/U9FmbqYfMcddTBpLf2oqlEjh5r10BWONOKx6tpUd/ALBgC34XNSUgRF6Q+D80hkayzO0s+ZAOH5dZCQydyGhh5xHdnscq9SRNvonmh4FtbkMAcsl81OkkB35jCnbqN3Ms8PbdzHDwujH/Bs2zvnuOovgce2Uo6U3/r39XZT2Jy7u34aUjSeLGvyldNsSf9Krwzt4xru0TFB+Zj/J0YapV1WfNv4M7Y9c3UMu+UpM4CnOVB5AoGhAPS5rO2gYJw3zeCfqgEFuExuZqMbZXRLap/fyA5ERpm8+oHabi+Yow1e1Lj+jHL7y1x0o1l0HAkzG9QnM3+pVBV0dlF7hSBHG1h5ajcOMqnjTB2Q4IzCUhAGvwbG2ZZn6E1KJUs+1PulqTnubZMiXQm7AbjAzBbhc0yFDnh7cZSF2oKKNsjsMgRs2zUoiPipMqIa+cyu2dbBxOur2viaXcUCgaEA10q4FWfQLFwcbso6VgaL0im1yp1SXRCqoZ6CeL6A2LyZScJ7fupHVBguIxFaMIZTGFHuGgVMti/MIYfThZM386Z8gL8LoAqp95OgKsm2WGyFca6rvrlJhmLGAgxULrU0HQ9eDNw/4jGVTmuW9OG0yfBJFABW1G9fsjJNSHyjxbX4p8e88RXn6AWU8KIgPcTeyYiYW7oeycEOQKXBRAvCVwKBoHxqIPY1wKfq3unBgkY+yDFjNx7ZAL0c0joxJJ3MLDBgmOKHT9k2uj5D5dWe43xZckuwqJMDqnUq3I0A2Pef+DVoHfbS2x1LySUzpMIUn8Eq4zlsvBwTdnDbXSZu1ZPXg4w3k7orWVAOKnhfNKnGoJIf1oI80gUX3OwcAP1TlKyzWCFhbvS6z7rcNJ1T3D6+lhU/rZtXkTvTCu/dUNducxkCgaEAmF+ZygyfmcXXmdInV5tqemRq9exCzvtsyNaXIT4zkzJoi3vKTBkkQrCadtdhLdbkGOJj0qbNlGRf5Ztsaa24fAnpNHYClFL32PHy+lWpaQvlpoApreMV1rcxzWFMc4JsT+UAe7mmvUC6m7YqMUdN3V7jWfSjf9cTs9tGN3d/sf8I7ja/0yHd8KmCsI0OpKkhxSGYM9OW1tR8ceTe1Ho+IQKBoQDELzIIUG4ND9QuvgwJUW0ys4Xo1BX5wNWP77GasAEb9EYB1uF7Apcp8yfGax+pRuqd/t0e5HDcd4JOhwfcWHAZYQqEm6d9yxq+eE0o/junEIVEPggNgcq3xJTryhq7kpzduj8GDdbq8PjuMFouJezjYHnbcidqAgLNfofqkj9cxAn5xIg2jhMIJh8RUqFlNEp8EKulTlGVTA8sj8Lf2Z1B", Base64.DEFAULT);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encoded);
        RSAPrivateKey debugprivateKey = (RSAPrivateKey) fact.generatePrivate(privateKeySpec);

// selftest (encrypt data)
        String OutPutEncryption = "debug.exponent.javatest2.txt";
        CheckCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        DataOutputStream cryptFile = new DataOutputStream(new FileOutputStream(OutPutEncryption));
        byte[] decryptedBuffer = new byte[] {100, 110, 112, 99, 64}; // some data 5 bytes
        CheckCipher.doFinal(decryptedBuffer, 0, 5, encryptedBuffer);
        cryptFile.write(encryptedBuffer);
        cryptFile.close();

// decrypt data from selftext or external source
        String SourceFile = "debug.exponent.javatest2.txt";
        CheckCipher.init(Cipher.DECRYPT_MODE, debugprivateKey);
        DataInputStream readCryptFileDeb = new DataInputStream(new FileInputStream(SourceFile));
        readCryptFileDeb.read(encryptedBuffer);
        readCryptFileDeb.close();
        byte[] Test = CheckCipher.doFinal(encryptedBuffer);
        System.out.println(new String(Test));
    }
}
