private static String HashPassword(String password)
{
    MessageDigest hash = MessageDigest.getInstance("SHA-256");
    byte[] utf8 = hash.digest(password.getBytes(StandardCharsets.UTF_8));

    return BytesToHex(hash.ComputeHash(utf8)); // ComputeHash?
}
