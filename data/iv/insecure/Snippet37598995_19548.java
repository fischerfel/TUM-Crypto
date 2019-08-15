import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Ex02 {
    private static final byte[] firstPart = {0x43, 0x72, 0x1a, 0x5b,
            (byte) 0x8e, (byte) 0xde, 0x5f, 0x57,
            (byte) 0x89, (byte) 0xf0, (byte) 0xdd, 0x28,
            0x68, (byte) 0x80};
    private static final byte[] SOLUTION = {0x43, 0x72, 0x1a, 0x5b,
            (byte) 0x8e, (byte) 0xde, 0x5f, 0x57,
            (byte) 0x89, (byte) 0xf0, (byte) 0xdd, 0x28,
            0x68, (byte) 0x8d, 0x63, (byte) 0xc5};
    private static final byte[] IV = {(byte) 0x80, (byte) 0x81, (byte) 0x82, (byte) 0x83,
            (byte) 0x84, (byte) 0x85, (byte) 0x86, (byte) 0x87,
            (byte) 0x88, (byte) 0x89, (byte) 0x8a, (byte) 0x8b,
            (byte) 0x8c, (byte) 0x8d, (byte) 0x8e, (byte) 0x8f};
    private static byte[] currentKey = new byte[IV.length];
    private static final byte[] chiffrat = Chiffrat.getAsHex();

    // For debugging purposes, this is set as class-variable
    private static int i;

    public static void main(String[] args) {
        Cipher c;

        // 20 Bits are missing, so 20 Bits to test
        int rounds = (int) Math.pow(2, 20);
        try {
            c = Cipher.getInstance("AES/CBC/NoPadding");

            // Test every possible key
            for (i = 0xd63c3; i < rounds; i++) {
                // For tracking progress
                if (i%100000==0 && i > 1) System.out.println(i + "/" + rounds);

                // Create Key out of first and second part
                // First convert i to a six digit hex number
                String tmp = Integer.toHexString(i);
                while (tmp.length() < 6) {
                    tmp = "0" + tmp;
                }

                // Convert String into byte array
                byte[] secondPart = new byte[tmp.length() / 2];
                for (int j = 0; j < tmp.length(); j+=2) {
                    secondPart[j / 2] = (byte) Integer.parseInt(tmp.substring(j, j + 2), 16);
                }

                // Copy first key part into the final key array
                System.arraycopy(firstPart, 0, currentKey, 0, firstPart.length);

                // Attach second part to the final key
                for (int j = 0; j < secondPart.length; j++) {
                    currentKey[currentKey.length - secondPart.length + j] |= secondPart[j];
                }

                // Decrypt
                SecretKeySpec secretKeySpec = new SecretKeySpec(currentKey, "AES");
                c.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(IV));
                byte[] result = c.doFinal(chiffrat);

                // Check if decrypted text is useful
                if (checkEnglish(result)) {
                    System.out.println("Key found!");
                    System.out.println("Counter: " + i);
                    System.out.println("Generated String: " + tmp);
                    System.out.println("First Part: 0x" + byteArrayToString(firstPart));
                    System.out.println("Second Part: 0x" + byteArrayToString(secondPart));
                    System.out.println("Used Key: 0x" + byteArrayToString(currentKey));
                    System.out.println("Result: " + new String(result));
                    break;
                }
            }
        } catch (Exception e) {
            System.out.print("ERROR: ");
            e.printStackTrace();
        }
    }

    private static boolean checkEnglish(byte[] b) {
        String tmp = new String(b).toLowerCase();
        return (tmp.contains("the") && tmp.contains("to") && tmp.contains("in") && tmp.contains("and"));
    }

    private static String byteArrayToString(byte[] bArr) {
        String result = "";
        for (byte b : bArr) {
            result += Integer.toHexString(Byte.toUnsignedInt(b));
        }
        return result;
    }
}
