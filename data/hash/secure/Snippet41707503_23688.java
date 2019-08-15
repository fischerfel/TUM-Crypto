public static String sha512Convert(String password) {
    try {
        MessageDigest sh = MessageDigest.getInstance("SHA-512");
        sh.reset();
        sh.update(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : sh.digest()) sb.append(Integer.toHexString(0xff & b));
        return sb.toString();
    } catch (NoSuchAlgorithmException e) {
        Logger.getLogger(UserLoginManaged.class.getName()).log(Level.SEVERE, null, e);
        throw new RuntimeException(e);
    }
}
