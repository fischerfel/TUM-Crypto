    public static String cipherPassword(String pwd, String salt) throws NoSuchAlgorithmException, UnsupportedEncodingException
{
    MessageDigest d = MessageDigest.getInstance("SHA-256");
    d.update(salt.getBytes("UTF-8"));
    byte[] hash = d.digest(pwd.getBytes("UTF-8"));

    StringBuilder sb = new StringBuilder();

    for(int i=0; i< hash.length ;i++)
    {
        sb.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
    }

    String pwdCifrada = sb.toString();

    return pwdCifrada;
}
