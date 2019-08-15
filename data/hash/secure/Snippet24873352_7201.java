public static byte[] SHAsum(byte[] convertme)
                throws NoSuchAlgorithmException {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(convertme);
        }

        private static String byteArray2Hex(final byte[] hash) {
            Formatter formatter = new Formatter();
            for (byte b : hash) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        }
