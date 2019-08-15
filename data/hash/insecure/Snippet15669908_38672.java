import java.security.MessageDigest;
import java.math.BigInteger;
import javax.xml.bind.DatatypeConverter;

public class TestHash3 {

    public static void main(String[] args) throws Exception {
        String hexString = "1234";

        /*
         * NB!
         * Before passing hex string to DatatypeConverter.parseHexBinary(),
         * we need to check if the hex sting is even-length, 
         * otherwise DatatypeConverter.parseHexBinary() will throw a
         * java.lang.IllegalArgumentException: hexBinary needs to be even-length
         */
        hexString = (hexString.length() % 2 == 0) ? hexString : "0" + hexString;
        byte[] bytes = DatatypeConverter.parseHexBinary(hexString);

        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] byteData = md.digest(bytes);

        System.out.println("1) SHA-1 hash for the hex string " + hexString + ": " +
                            bytesToHexString1(byteData));
        System.out.println("2) SHA-1 hash for the hex string " + hexString + ": " +
                            bytesToHexString2(byteData));
        System.out.println("3) SHA-1 hash for the hex string " + hexString + ": " +
                            bytesToHexString3(byteData));
        System.out.println("4) SHA-1 hash for the hex string " + hexString + ": " +
                            bytesToHexString4(byteData));
    }

    public static String bytesToHexString1(byte[] bytes) {
        StringBuffer hexBuffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            hexBuffer.append(Integer.toString((bytes[i] & 0xFF) + 0x100, 16).substring(1));
        }

        return hexBuffer.toString();
    }

    public static String bytesToHexString2(byte[] bytes) {
        StringBuffer hexBuffer = new StringBuffer(bytes.length * 2);
        for (byte b: bytes) {
            int n = b & 0xFF;   // casting to integer to avoid problems with negative bytes
            if (n < 0x10) {
                hexBuffer.append("0");
            }
            hexBuffer.append(Integer.toHexString(n));
        }

        return hexBuffer.toString();
    }       

    public static String bytesToHexString3(byte[] bytes) {
        StringBuffer hexBuffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hexString = Integer.toHexString(0xff & bytes[i]);
            // NB! E.g.: Integer.toHexString(0x0C) will return "C", not "0C"            
            if (hexString.length() == 1) {
                hexBuffer.append('0');
            }
            hexBuffer.append(hexString);
        }

        return hexBuffer.toString();
    }

    public static String bytesToHexString4(byte[] bytes) {
        String hexString = new BigInteger(1, bytes).toString(16);

        /*
         * NB!
         * We need an even-length hex string to propely represent bytes in hexadecimal.
         * A hexadecimal representation of one byte consists of two hex digits.
         * If the value is less than 16 (dec), it is prepended with zero
         * E.g.:
         * 1  (byte)    ==> 01 (hex)    // pay attention to the prepended zero
         * 15 (byte)    ==> 0F (hex)
         * 16 (byte)    ==> 10 (hex)    // no need to prepend
         * 255(byte)    ==> FF (hex)
         *
         * BigInteger.toString(16) can return both even and odd-length hex strings.
         * E.g.:
         * byte[] bytes = {15, 16}  // two bytes
         * BigInteger(1, bytes).toString(16) will produce (NB!): f10
         * But we need (NB!): 0f10
         * So we must check if the resulting hex string is even-length,
         * and if not, prepend it with zero.
         */
        return ((hexString.length() % 2 == 0) ? hexString : "0" + hexString);
    }
}
