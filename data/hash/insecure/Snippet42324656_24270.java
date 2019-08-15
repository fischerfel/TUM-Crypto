private static String convertNumToCharacters(String phoneNumber) {
    byte[] digits = new byte[phoneNumber.length()];
    for (int i = 0; i < digits.length; i++) {
        digits[i] = (byte) Character.digit(phoneNumber.charAt(i), 10);
    }
    try {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        return Base64.getEncoder().encodeToString(md.digest(digits));
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    return null;
}
