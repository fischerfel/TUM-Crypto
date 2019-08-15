public String getSha256(String text, String encoding){
    String sha = "";
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(text.getBytes(encoding));
        byte[] digest = md.digest();
        sha = new String(digest);
        sha = sha.replace("\n", "");
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
        Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
    }
    return sha;
}
