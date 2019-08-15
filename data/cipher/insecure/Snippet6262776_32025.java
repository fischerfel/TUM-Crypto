public class App {

    public static void main(String[] args) throws Exception {
        TCPPacket packet = new TCPPacket(481, 516, 23, 42, true, false, false, false, false, false, false, false, 10, 10);

        final char[] password = "secretpass".toCharArray();
        final byte[] salt = "a9v5n38s".getBytes();

        // Create key
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(password, salt, 1024, 128);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        // Init ciphers
        Cipher cipher = Cipher.getInstance("AES");
        Cipher dcipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        dcipher.init(Cipher.DECRYPT_MODE, secret);

        // Encrypt packet
        SealedObject so = new SealedObject(packet, cipher);

        // Decrypt packet
        TCPPacket decryptedPacket = (TCPPacket) so.getObject(dcipher);

        System.out.println(decryptedPacket.first);
    }

    private static class TCPPacket implements Serializable {
        private int first;
        public TCPPacket(final int _first, final int i1, final int i2, final int i3, final boolean b, final boolean b1,
                         final boolean b2, final boolean b3, final boolean b4, final boolean b5, final boolean b6,
                         final boolean b7, final int i4, final int i5) {

            first = _first;
        }
        public int getFirst() {
            return first;
        }

    }
}
