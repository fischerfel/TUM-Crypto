public void encrypeUsername(String sessionid)
    {
        byte[] defaultBytes = sessionid.getBytes();
        try
        {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
            {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            System.out.println("sessionid " + sessionid + " md5 version is " + hexString.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



    }
