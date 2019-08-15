// I'm not author of generateKey method so I've no idea if is it correct
private static byte[] generateKey(String pass) throws UnsupportedEncodingException, NoSuchAlgorithmException
{
MessageDigest sha = MessageDigest.getInstance("SHA-256");
byte[] passBytes = pass.getBytes("ASCII");
byte[] sha256Bytes = sha.digest(passBytes);


byte[] key = new byte[16];
int j = 0;
for (int i = 0; i < sha256Bytes.length; i++)
{
    if (i % 2 == 0)
    {
    key[j] = sha256Bytes[i];
    j++;
    }
}
return key;
}

public static Cipher getEncryptCipher(String pass)
{
try
{
    SecretKeySpec skeySpec = new SecretKeySpec(generateKey(pass), "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    return cipher;
}
catch (Exception ex) // just for clarity
{
    Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
}
return null;
}

public static Cipher getDecryptCipher(String pass)
{
try
{
    SecretKeySpec skeySpec = new SecretKeySpec(generateKey(pass), "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    return cipher;
}
catch (Exception ex) // just for clarity
{
    Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
}
return null;
}
