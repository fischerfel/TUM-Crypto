    private Key generateKey() throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA");
            String passphrase = "blahbl blahbla blah";
    digest.update(passphrase.getBytes());
    return new SecretKeySpec(digest.digest(), 0, 16, "AES");
}
