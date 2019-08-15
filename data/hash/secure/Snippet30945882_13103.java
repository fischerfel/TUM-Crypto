String pswd="password";
String storedPswd="5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";
//first as a byte[]

Arrays.equals(hashWord(pswd),storedPswd.getBytes("UTF-8") );
...
private byte[] hashWord(String word)     {
    try {
        return MessageDigest.getInstance("SHA-256").digest(word.getBytes("UTF-8"));
    } catch (Exception e)        {
       throw new BadCredentialsException("Could not hash supplied password", e);
    }
}
