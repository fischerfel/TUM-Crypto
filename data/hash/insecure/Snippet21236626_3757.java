public class CreateRSAPublicKeyID {

    /**
     * Creates a key ID for a given key.
     * 
     * @param key the key
     * @return the key ID for the given key
     */
    public static byte[] createKeyID(Key key) {

        if (key instanceof RSAKey) {
            RSAKey rsaKey = (RSAKey) key;
            BigInteger modulus = rsaKey.getModulus();
            if (modulus.bitLength() % Byte.SIZE != 0) {
                throw new IllegalArgumentException("This method currently only works with RSA key sizes that are a multiple of 8 in bits");
            }
            final byte[] modulusData = i2os(modulus, modulus.bitLength() / Byte.SIZE);
            MessageDigest sha1;
            try {
                sha1 = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException("SHA-1 message digest should be available in any Java SE runtime", e);
            }
            return sha1.digest(modulusData);
        }

        throw new UnsupportedOperationException("Key type not supported");
    }

    /**
     * Integer to octet string (I2OS) creates a fixed size, left padded, big-endian octet string representation for
     * a given integer.
     * 
     * @param i the integer
     * @param octets the number of octets (bytes)
     * @return the octet string representation of i
     */
    public static byte[] i2os(BigInteger i, int octets) {

        if (i.bitLength() > octets * Byte.SIZE) {
            throw new IllegalArgumentException("i does not fit in " + octets + " octets");
        }

        final byte[] is = i.toByteArray();
        if (is.length == octets) {
            return is;
        }

        final byte[] ius = new byte[octets];
        if (is.length == octets + 1) {
            System.arraycopy(is, 1, ius, 0, octets);
        } else {
            System.arraycopy(is, 0, ius, octets - is.length, is.length);
        }
        return ius;
    }

    public static String toHex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            sb.append(String.format("%02X", data[i]));
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair pair = kpg.generateKeyPair();
        byte[] keyID = createKeyID(pair.getPublic());
        System.out.println(toHex(keyID));
    }
}
