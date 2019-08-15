public String processSHA512(String pw, String salt, int rounds)
{
    try {
        md = MessageDigest.getInstance("SHA-512");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
        throw new RuntimeException("No Such Algorithm");
    }

    String result = hashPw(pw, salt, rounds);
    System.out.println(result);
    return result;
}

private static String hashPw(String pw, String salt, int rounds) {
    byte[] bSalt;
    byte[] bPw;

    String appendedSalt = new StringBuilder().append('{').append(salt).append('}').toString();

    try {
        bSalt = appendedSalt.getBytes("ISO-8859-1");
        bPw = pw.getBytes("ISO-8859-1");
    } catch (UnsupportedEncodingException e) {
        throw new RuntimeException("Unsupported Encoding", e);
    }

    byte[] digest = run(bPw, bSalt);
    Log.d(LCAT, "first hash: " + Base64.encodeBytes(digest));
    for (int i = 1; i < rounds; i++) {
        digest = run(digest, bSalt);
    }

    return Base64.encodeBytes(digest);
}

private static byte[] run(byte[] input, byte[] salt) {
    md.update(input);
    return md.digest(salt);
}
