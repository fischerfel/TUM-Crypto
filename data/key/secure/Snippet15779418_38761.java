    private String encrypt(String string, String key) {
    StringBuilder enc = new StringBuilder();
    try {
        Mac mac = Mac.getInstance("HMACSHA256");
        SecretKeySpec secret = new SecretKeySpec(key.getBytes(), "HMACSHA256");
        mac.init(secret);
        byte[] digest = mac.doFinal(string.getBytes());
        for (byte b : digest) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                enc.append('0');
            enc.append(hex);
        }
    } catch (Exception e) {
    }
    return enc.toString();
}
