import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

final class JceSha1Test {
    public static void main(final String ... args) {
        final String strHashInput = "JAO21V279RSNHYGX23L0";
        final byte[] strHash = hashPassword(strHashInput);
        System.out.println("strHash: " + toHex(strHash));
        final ByteBuffer strHashBuffer = ByteBuffer.wrap(strHash);
        strHashBuffer.order(ByteOrder.LITTLE_ENDIAN);
        final long test = strHashBuffer.getLong();
        System.out.println(test);
    }

    public static byte[] hashPassword(final String password) {
        final byte[] encodedPassword = password.getBytes(StandardCharsets.UTF_8);
        final MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance("SHA-1");
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
        return algorithm.digest(encodedPassword);
    }

    public static String toHex(final byte[] data) {
        final StringBuilder sb = new StringBuilder(data.length * 2);
        for (int i = 0; i < data.length; i++) {
            sb.append(String.format("%02X", data[i]));
        }
        return sb.toString();
    }
}
