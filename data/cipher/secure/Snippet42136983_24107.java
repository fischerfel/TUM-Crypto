private static byte[] ivBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04,
    0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };

// keyBytes
private static byte[] keyBytes = new byte[] { 0x60, 0x3d, (byte) 0xeb,
    0x10, 0x15, (byte) 0xca, 0x71, (byte) 0xbe, 0x2b, 0x73,
    (byte) 0xae, (byte) 0xf0, (byte) 0x85, 0x7d, 0x77, (byte) 0x81,
    0x1f, 0x35, 0x2c, 0x07, 0x3b, 0x61, 0x08, (byte) 0xd7, 0x2d,
    (byte) 0x98, 0x10, (byte) 0xa3, 0x09, 0x14, (byte) 0xdf,
    (byte) 0xf4 };

public string AES_Encrypt(string ToBeEncrypted, string password)
{
    RijndaelManaged aes = new RijndaelManaged();

    aes.BlockSize = 128;
    aes.KeySize = 256;

    // It is equal in java 
    /// Cipher _Cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");    
    aes.Mode = CipherMode.CBC;
    aes.Padding = PaddingMode.PKCS7;

    SHA256 sha = new SHA256Managed();
    aes.Key = sha.ComputeHash(Encoding.UTF8.GetBytes(password));
    aes.IV = ivBytes;

    ICryptoTransform encrypto = aes.CreateEncryptor();

    byte[] plainTextByte = ASCIIEncoding.UTF8.GetBytes(ToBeEncrypted);
    byte[] CipherText = encrypto.TransformFinalBlock(plainTextByte, 0, plainTextByte.Length);

    string enc = BitConverter.ToString(CipherText).Replace("-", string.Empty);
    return Convert.ToBase64String(CipherText) + "----"+ Convert.ToBase64String(ASCIIEncoding.UTF8.GetBytes(enc));
}
