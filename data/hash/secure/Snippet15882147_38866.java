package android.security;

import java.security.NoSuchAlgorithmException;

public class MessageDigest
{
    private java.security.MessageDigest instance;

    public MessageDigest() {}

    private MessageDigest(java.security.MessageDigest instance)
    {
        this.instance = instance;
    }

    public static MessageDigest getInstance(String algorithm) throws NoSuchAlgorithmException
    {
        if (algorithm == null) return null;

        try
        {
            if (algorithm.equals("SHA-1"))
                return (MessageDigest) Class.forName("android.security.Sha1MessageDigest").newInstance();
            else if (algorithm.equals("MD5"))
                return (MessageDigest) Class.forName("android.security.Md5MessageDigest").newInstance();
        }
        catch (Exception e) {}

        return new MessageDigest(java.security.MessageDigest.getInstance(algorithm));
    }

    public void update(byte[] input)
    {
        instance.update(input);
    }

    public byte[] digest()
    {
        return instance.digest();
    }

    public byte[] digest(byte[] input)
    {
        return instance.digest(input);
    }
}
