import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Example {

public static void main(String[] args) throws Exception {

    final String inputString = "Hello MD5";

    System.out.println("MD5 hex for '" + inputString + "' :");
    System.out.println(getMD5Hex(inputString));
}

public static String getMD5Hex(final String inputString) throws NoSuchAlgorithmException {

    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(inputString.getBytes());

    byte[] digest = md.digest();

    return convertByteToHex(digest);
}

private static String convertByteToHex(byte[] byteData) {

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < byteData.length; i++) {
        sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
    }

    return sb.toString();
}
}
