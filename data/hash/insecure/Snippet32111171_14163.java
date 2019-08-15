private static byte[] getMD5(String input) {
        try {
       byte[] bytesOfMessage = input.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(bytesOfMessage);
        } catch (Exception e) {
        }
        return null;
    }
