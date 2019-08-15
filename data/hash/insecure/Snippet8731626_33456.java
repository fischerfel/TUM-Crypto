public synchronized String encrypt(String token) {
    try {
        MessageDigest sha = MessageDigest.getInstance("SHA");
        sha.reset();
        sha.update(token.getBytes("UTF-8"));
        byte[] raw = sha.digest();
        System.out.println("raw = " + raw.toString());
        String hash = Base64.encodeBase64(raw).toString();
        return hash;
    } catch (Exception e) {
    }

    return token;
}
