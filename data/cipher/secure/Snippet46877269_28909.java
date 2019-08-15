import java.util.Base64;

public class Enc2 {
    public static void main (String[] arg) {
        System.out.println(encryptSomeNumber("1234567812345678"));
    }

    public static String encryptSomeNumber(final String SomeNumber){
        String encryptedSomeNum = "";
        String ALGO = "AES";

        try {
            String myKey = "DLDiGPqGysAow3II";
            byte[] keyBytes = myKey.getBytes("UTF-8");

            java.security.Key encryptkey = new javax.crypto.spec.SecretKeySpec(keyBytes, ALGO);
            javax.crypto.Cipher c;
            c = javax.crypto.Cipher.getInstance(ALGO);
            c.init(javax.crypto.Cipher.ENCRYPT_MODE, encryptkey);
            byte[] encVal = c.doFinal(SomeNumber.getBytes());

            byte[] encodedBytes = Base64.getEncoder().encode(encVal);
            String s = new String(encodedBytes);

            encryptedSomeNum = s;
        } catch (Exception e) {
            System.out.println("error when encrypting number");
            return encryptedSomeNum;
        }
        return encryptedSomeNum;
    }
}
