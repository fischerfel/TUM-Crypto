public static String sha(String base) {
    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        StringBuilder hexString = new StringBuilder();
        for(byte b: digest.digest(base.getBytes(StandardCharsets.UTF_8)))
            hexString.append(String.format("%02x", b&0xff));
        return hexString.toString();
    } catch(NoSuchAlgorithmException ex){
        throw new RuntimeException(ex);
    }
}
