public static String getPassword(String spId, String password, String timestamp) 
{
    try {
        String dgStr = spId + password + timestamp;

        byte[] enc = MessageDigest.getInstance("SHA-256").digest(dgStr.getBytes());
        return new String(Base64.encodeBase64(new String(Hex.encodeHex(enc)).toUpperCase().getBytes()));
    } catch (NoSuchAlgorithmException ex) {
        ex.printStackTrace();
    }
    return "";
}
