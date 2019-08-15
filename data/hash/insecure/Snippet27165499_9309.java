public static String MD5Encode(String input, String salt) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] hash = null;

            try {
                messageDigest.update(salt.getBytes("UTF-8"));
                messageDigest.update(input.getBytes("UTF-8"));
                hash = messageDigest.digest();
            } catch (UnsupportedEncodingException exception) {
                logger.error("MD5Encoder:Encode:" + exception.toString());
            }

            if (hash != null) {
                StringBuilder output = new StringBuilder(2 * hash.length);

                for (byte b : hash) {
                    output.append(String.format("%02x", b & 0xff));
                }

                return output.toString();
            }
        } catch (NoSuchAlgorithmException exception) {
            logger.error("MD5Encoder:Encode:" + exception.toString());
        }

        return null;
    }
