public static String hash(String body, String secret) {
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(body.getBytes("UTF-8"));

        byte[] bytes = md.digest(secret.getBytes("UTF-8")); 

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    } catch (Exception e) {
        throw new RuntimeException();
    }
}
