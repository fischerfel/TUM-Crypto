    public class UtilsEncrypt {

        /**
         * @param digest 
                  encrypted message
         * 
         * @return String 
                     result in Hexadecimal format
         */
        private static String toHexadecimal(byte[] digest) {
            String hash = "";
            for (byte aux : digest) {
                int b = aux & 0xff;
                if (Integer.toHexString(b).length() == 1)
                    hash += "0";
                hash += Integer.toHexString(b);
            }
            return hash;

        }

        /***
         * Encrypt a message through an algorithm
         * 
         * @param message
         *            text to encrypt
         * @param algorithm
         *            MD2, MD5, SHA-1, SHA-256, SHA-384, SHA-512
         * @return encrypted message
         */
        public static String getStringMessageDigest(String message, String algorithm) {
            byte[] digest = null;
            byte[] buffer = message.getBytes();
            try {
                MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
                messageDigest.reset();
                messageDigest.update(buffer);
                digest = messageDigest.digest();
            } catch (NoSuchAlgorithmException ex) {
                // Do something
            }
            return toHexadecimal(digest);
        }
    }
