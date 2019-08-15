String password = "abcd";
String salt = "5f8f041b75042e56";

try {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");

    return digest.digest((password + salt).getBytes("UTF-8"));
} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
    return null;
}
