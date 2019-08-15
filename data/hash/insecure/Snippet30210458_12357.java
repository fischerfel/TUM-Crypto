public class RandomGenerator {
          private static SecureRandom prng;
          private static final Logger LOG = LoggerFactory
                    .getLogger(AuthTokenGenerator.class);
            static {
                try {
                    // Initialize SecureRandom
                    prng = SecureRandom.getInstance("SHA1PRNG");
                } catch (NoSuchAlgorithmException e) {
                    LOG.info("ERROR while intantiating Secure Random:   " + prng);
            }
        }
        /**
         * @return
         */
        public static String getToken() {
            try {
                LOG.info("About to Generate Token in getToken()");
                String token;
                // generate a random number
                String randomNum = Integer.toString(prng.nextInt());
                // get its digest
                MessageDigest sha = MessageDigest.getInstance("SHA-1");
                byte[] result = sha.digest(randomNum.getBytes());
                token = hexEncode(result);
                LOG.info("Token in getToken():   " + token);
                return token;
            } catch (NoSuchAlgorithmException ex) {
                return null;
            }
        }
        /**
         * @param aInput
         * @return
         */
        private static String hexEncode(byte[] aInput) {
            StringBuilder result = new StringBuilder();
            char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
                    'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
            for (byte b : aInput) {
                result.append(digits[(b & 0xf0) >> 4]);
                result.append(digits[b & 0x0f]);
            }
            return result.toString();
        }
}
