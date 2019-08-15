public static String encodePassword(String password) {
    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes("UTF-8")); //I've tried messing around with the encoding
        String result = "";
        for(int i : hash) {
            result += String.format("%02x", 0xff & i);
            }
        return result;
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
