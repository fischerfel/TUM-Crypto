    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);


            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32)
                hashtext = "0" + hashtext;
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
