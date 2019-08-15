public class AesCbc {
    private static String PLAIN = "usr/passwd@bizdb:127.0.0.1:5432";

    public static void main(String[] args) throws Exception {
        Cipher aesCipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec("1234567890ABCDEF".getBytes(), "AES");
        IvParameterSpec iv = new IvParameterSpec("fedcba0987654321".getBytes());

        aesCipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
        byte[] rawBytes = PLAIN.getBytes();
        byte[] aligned;
        int mod = rawBytes.length % 16; // prevent javax.crypto.IllegalBlockSizeException
        if (mod == 0) {
            aligned = new byte[rawBytes.length];
        } else {
            aligned = new byte[rawBytes.length + 16 - mod];
        }
        System.arraycopy(rawBytes, 0, aligned, 0, rawBytes.length);
        byte[] cipherBytes = aesCipher.doFinal(aligned);
        String base64Result = Base64.getEncoder().encodeToString(cipherBytes);
        System.out.println("cipher:[" + base64Result + "], rawBytes.length=" + rawBytes.length + ", mod=" + mod);

        aesCipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
        cipherBytes = Base64.getDecoder().decode(base64Result);
        aligned = aesCipher.doFinal(cipherBytes);
        int posNil;
        for (posNil = 0; posNil < aligned.length; posNil++) {
            if (aligned[posNil] == 0x00)
                break;
        }
        rawBytes = new byte[posNil];
        System.arraycopy(aligned, 0, rawBytes, 0, posNil);
        String plain = new String(rawBytes);
        System.out.println("plain:[" + plain + "], posNil=" + posNil + ", aligned.length=" + aligned.length);
    }
}
