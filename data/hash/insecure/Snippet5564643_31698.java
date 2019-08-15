private static String getSHA1FromFileContent(String filename)
    {

        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            //byte[] buffer = new byte[65536]; //created at start.
            InputStream fis = new FileInputStream(filename);
            int n = 0;
            while (n != -1)
            {
                n = fis.read(buffer);
                if (n > 0)
                {
                    digest.update(buffer, 0, n);
                }
            }
            byte[] digestResult = digest.digest();
            return asHex(digestResult);
        }
        catch (Exception e)
        {
            return null;
        }
    }
