    try
    {
        MessageDigest digester = MessageDigest.getInstance("MD5");
        digester.update("test".getBytes("UTF-8"));
        byte[] digest = digester.digest();
    }
    catch (Throwable e1)
    {
        e1.printStackTrace();
        // Class available but not functional
    }
