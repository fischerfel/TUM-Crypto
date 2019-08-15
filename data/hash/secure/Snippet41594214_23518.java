   public static String Encode_SHA512(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte [] inputBytes = input.getBytes("UTF-16LE");
        byte[] digest = md.digest(inputBytes);
        return Base64.getEncoder().encodeToString(digest);
    }
