public String hash256(String txt){
    MessageDigest sha = MessageDigest.getInstance("SHA-256");
    sha.update(txt.getBytes());
    byte[] digest = sha.digest();
    return bytesToString(digest);
}
