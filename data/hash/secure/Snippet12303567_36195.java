public static String toRIPEMD160(String in)
{
    byte[] addr = in.getBytes();
    byte[] out = new byte[20];
    RIPEMD160Digest digest = new RIPEMD160Digest();
    byte[] sha256 = sha256(addr);
    digest.update(sha256,0,sha256.length);
    digest.doFinal(out,0);
    return getHexString(out);
}

public static byte[] sha256(byte[] data)
{
    byte[] sha256 = new byte[32];
    try
    {
        sha256 = MessageDigest.getInstance("SHA-256").digest(data);
    }
    catch(NoSuchAlgorithmException e)
    {}

    return sha256;
}
