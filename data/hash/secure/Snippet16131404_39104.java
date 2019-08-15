try
{
    MessageDigest md = MessageDigest.getInstance("SHA-512");
    md.update(password.getBytes());
    byte[] raw = md.digest();
    encodedPassword = (new BASE64Encoder()).encode(raw);
}
