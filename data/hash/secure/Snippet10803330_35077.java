public static void main(String[] args) 
    throws NoSuchAlgorithmException, UnsupportedEncodingException {

    char[] password = new char[]{'a', 'b', 'c'};

    MessageDigest messageDigest = null;
    messageDigest = MessageDigest.getInstance("SHA-256");

    byte[] hash = null;

    // This is how you convert a char array into a String without reencoding it into a different set of characters.
    String passwordString = new String(password);

    hash = messageDigest.digest(passwordString.getBytes("UTF-8"));
    StringBuilder sb = new StringBuilder();
    for (byte b : hash) {
        sb.append(String.format("%02x", b));
    }
    String passwordHashed = sb.toString();
    System.out.println(passwordHashed);
}
