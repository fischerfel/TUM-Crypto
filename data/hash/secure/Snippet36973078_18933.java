private static String generateHash(String input) throws NoSuchAlgorithmException {
    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
    messageDigest.update(input.getBytes());
    String encryptedString = new String(messageDigest.digest());
    System.out.println("encryptedString :: " + encryptedString);
    return encryptedString;
}
