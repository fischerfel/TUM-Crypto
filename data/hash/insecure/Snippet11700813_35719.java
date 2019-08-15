    public static string ComputeHash(params byte[][] bytes)
    {
        if (bytes == null) throw new ArgumentNullException("bytes");
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        foreach (var item in bytes)
        {
            if (item == null)
                if (bytes == null) throw new ArgumentNullException("bytes", "Inner array is null");
            digest.update(item);
        }
        string s = (new java.math.BigInteger(digest.digest())).toString(16);
        return s;
    }
