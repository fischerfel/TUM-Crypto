public static String convertToMD5Hash(final String plainText){
            MessageDigest messageDigest = null;

            try {
                messageDigest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                LOGGER.warn("For some wierd reason the MD5 algorithm was not found.", e);
            }

            messageDigest.reset();
            messageDigest.update(plainText.getBytes());
            final byte[] digest = messageDigest.digest();
            final BigInteger bigInt = new BigInteger(1, digest);
            String hashtext = bigInt.toString(8);

            return hashtext;
}
