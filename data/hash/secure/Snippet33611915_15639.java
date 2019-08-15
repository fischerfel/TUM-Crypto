public static String sha(String base) {
    try{
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
        String encoded = Base64.getEncoder().encodeToString(hash);
        assert encoded.length()==88 && encoded.endsWith("==");
        return encoded.substring(0, 86);
    } catch(NoSuchAlgorithmException ex){
        throw new RuntimeException(ex);
    }
