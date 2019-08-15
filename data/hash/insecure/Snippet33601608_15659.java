public class SAPISIDHASH {

    public static void main(String [] args) {

        String sapisid = "b4qUZKO4943exo9W/AmP2OAZLWGDwTsuh1";
        String origin = "https://hangouts.google.com";
        String sapisidhash = "1447033700279" + " " + sapisid + " " + origin;
        System.out.println("SAPISID:\n"+ hashString(sapisidhash));
        System.out.println("Expecting:");
        System.out.println("38cb670a2eaa2aca37edf07293150865121275cd");

    }

    private static String hashString(String password)
    {
        String sha1 = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return sha1;
    }

    private static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
