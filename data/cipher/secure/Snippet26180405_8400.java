public java.security.PrivateKey m_privateKey;

public byte[] Decrypt(byte[] input)
{
    Cipher cipher;
    byte[] cipherData = null;
    cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, m_privateKey);
    cipherData = cipher.doFinal(input);
    return cipherData;
}
