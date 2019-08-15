public class DoubleSHA256 {

    final protected static char[] hexArray = "0123456789abcdef".toCharArray();

    public static String gen(String input) {

        MessageDigest digester = null;
        try {
            digester = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bytesToHex(digester.digest(digester.digest(input.getBytes())));

    }

    private static String bytesToHex(final byte[] bytes)
    {
        char[] hexChars = new char[bytes.length * 2];

        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }

}
