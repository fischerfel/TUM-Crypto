public String SaltedHash(String password)
{
    Hash = new String(ComputeHash(password.toCharArray(), Salt.getBytes()));
    return Hash;
}
public static byte[] ComputeHash(char[] password, byte[] salt)
{
    PBEKeySpec spec = new PBEKeySpec(password, salt, 1000, 256);
    Arrays.fill(password, Character.MIN_VALUE);
    try
    {
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return skf.generateSecret(spec).getEncoded();
    }
    catch (NoSuchAlgorithmException | InvalidKeySpecException e)
    {
        throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
    }
    finally
    {
        spec.clearPassword();
    }
}
