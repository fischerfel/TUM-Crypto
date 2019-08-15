        package com.password;

        import java.security.InvalidKeyException;
        import java.security.Key;
        import java.security.NoSuchAlgorithmException;
        import java.security.SignatureException;
        import java.util.Formatter;
        import javax.crypto.Mac;
        import javax.crypto.spec.SecretKeySpec;

        public class Sha256 {

     //Main Method that have the String values and key 
            public static void main(String s[]) {
                try {

                    String str ="HelloWorld"; //String Values
                    String key = "test@12345"; //Secret Key

                    String encry = hashMac(str, key);
//call the hashMac Method that encrypt the String using key and return the encrypted values....
                    System.out.println("Encryption : " + encry);
                } catch (SignatureException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
    //hashMac Method that encrypt the data and convert into hex values...    
            public static String hashMac(String text, String secretKey)
                    throws SignatureException {

                try {
                    Key sk = new SecretKeySpec(secretKey.getBytes(), HASH_ALGORITHM);
                    Mac mac = Mac.getInstance(sk.getAlgorithm());
                    mac.init(sk);
                    final byte[] hmac = mac.doFinal(text.getBytes());
                    return toHexString(hmac);//call toHexString Methods....
                } catch (NoSuchAlgorithmException e1) {
                    // throw an exception or pick a different encryption method
                    throw new SignatureException(
                            "error building signature, no such algorithm in device "
                                    + HASH_ALGORITHM);
                } catch (InvalidKeyException e) {
                    throw new SignatureException(
                            "error building signature, invalid key " + HASH_ALGORITHM);
                }
            }

            private static final String HASH_ALGORITHM = "HmacSHA256";
    //toHexString Method...
            public static String toHexString(byte[] bytes) {
                StringBuilder sb = new StringBuilder(bytes.length * 2);

                Formatter formatter = new Formatter(sb);
                for (byte b : bytes) {
                    formatter.format("%02x", b);
                }

                return sb.toString();

            }
        }
