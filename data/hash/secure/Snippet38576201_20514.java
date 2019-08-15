 public static byte[] encrypt(String password) {
        try {
            BASE64Encoder be = new BASE64Encoder();
            MessageDigest md = MessageDigest.getInstance("sha-512");
            md.update(password.getBytes());
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }
