public static String sha(String base) {
    try{
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    } catch(NoSuchAlgorithmException ex){
        throw new RuntimeException(ex);
    }
}
