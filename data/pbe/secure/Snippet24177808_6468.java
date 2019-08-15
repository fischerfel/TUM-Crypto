 private static final int ITERATIONS = 10000;
        private static final int KEY_LENGTH = 256; // bits

        public static String hashPassword(String password, String salt)
                throws NoSuchAlgorithmException, InvalidKeySpecException {
            char[] passwordChars = password.toCharArray();
            byte[] saltBytes = salt.getBytes();

            PBEKeySpec spec = new PBEKeySpec(passwordChars, saltBytes, ITERATIONS,
                    KEY_LENGTH);
            SecretKeyFactory key = SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA1");
            byte[] hashedPassword = key.generateSecret(spec).getEncoded();
            return String.format("%x", new BigInteger(hashedPassword));
        }

        public static void main(String[] args) throws Exception {
            System.out.println(hashPassword("abcd1234",
                    "6c646576656c6f7065726c3139383540676d61696c2e636f6d"));
    }
