public String generateKey(String title, String userName){
    char[] hexDigits = "0123456789abcdef".toCharArray();
    String source;
    String MD5 = null;
    byte[] digest = null;
    source = title + "balh" + userName ;
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        digest = md.digest(source.getBytes("UTF-16"));

        StringBuilder sb = new StringBuilder(32);
        for (byte b : digest)
        {
            sb.append(hexDigits[(b >> 4) & 0x0f]);
            sb.append(hexDigits[b & 0x0f]);
        }
        System.out.println("Gened KEY ===="+sb.toString());
        return sb.toString();
    } catch (Exception e) {
    }
    return "";
}
