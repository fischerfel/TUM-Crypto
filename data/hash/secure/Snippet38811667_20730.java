public String getSha512Hash(String password, String saltValue) throws NoSuchAlgorithmException{
    String text = saltValue + password ;
    MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
    byte[] bytes = messageDigest.digest( text.getBytes() );
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < bytes.length; ++i) {
        sb.append(Integer.toHexString((bytes[i] & 0xFF) | 0x100).substring(1,3));
    }
    return sb.toString();
}
