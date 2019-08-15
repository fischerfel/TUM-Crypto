public class Encryption {

    // Possibly use files for saving keys
    private static String PUBLIC_KEY_FILE = "Public.key";
    private static String PRIVATE_KEY_FILE = "Private.key";

    Key publicKey = null;
    Key privateKey = null;
    KeyPairGenerator kpg;
    KeyPair kp;
    Cipher cipher;

    public Encryption() {

        try {
            kpg = KeyPairGenerator.getInstance("RSA");
            kp = kpg.generateKeyPair();
            publicKey = kp.getPublic();
            privateKey = kp.getPrivate();
        } catch (Exception e) {
            Log.e("Encryption", "RSA KeyPair Error...");
            e.printStackTrace();
        }
    }

    // Encode
    public byte[] encode(byte[] bytesToEncode) {
        byte[] encodedBytes = null;

        try {
            // Basic Cipher (mode/padding excluded for now)
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            encodedBytes = cipher.doFinal(bytesToEncode);
        } catch (Exception e) {
            Log.e("Encryption", "RSA Encoding Error...");
            e.printStackTrace();
        }

        return encodedBytes;
    }

    public byte[] decode(byte[] encodedBytes) {
        byte[] decodedBytes = null;

        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            decodedBytes = cipher.doFinal(encodedBytes);
        } catch (Exception e) {
            Log.e("Encryption", "RSA Decoding Error...");
            e.printStackTrace();
        }

        return decodedBytes;
    }
}
