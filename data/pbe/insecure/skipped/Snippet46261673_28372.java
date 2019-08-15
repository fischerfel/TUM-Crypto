public static string decrypt(string data)
{
    byte[] dataBytes = Convert.FromBase64String(data);
    SecretKey secretKey = getSecretKey(hashTheKey("ABCD"));

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    cipher.init(2, secretKey, new IvParameterSpec(new byte[16]),
            SecureRandom.getInstance("SHA1PRNG"));
    var x = cipher.doFinal(dataBytes);
    return System.Text.Encoding.UTF8.GetString(x);
}
public static SecretKey getSecretKey(char[] key)
{
    var secretKeyType = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    var secretkey = secretKeyType.generateSecret(new PBEKeySpec(key,
            System.Text.Encoding.UTF8
                .GetBytes("ABCD"),
            100, 128)).getEncoded();

    return new SecretKeySpec(secretkey, "AES/CBC/PKCS5Padding");
}
public static char[] hashTheKey(string key)
{
    MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
    messageDigest.update(System.Text.Encoding.UTF8.GetBytes(key));
    return Convert.ToBase64String(messageDigest.digest()).ToCharArray();
}
