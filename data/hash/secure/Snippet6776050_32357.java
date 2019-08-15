public String getHash(String password, String salt) throws Exception {
    String input = password + salt;
    MessageDigest md = MessageDigest.getInstance(SHA-512);
    byte[] out = md.digest(input.getBytes());
    return HexEncoder.toHex(out);
}
