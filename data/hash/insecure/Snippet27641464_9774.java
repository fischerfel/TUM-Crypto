public class MD5Hash
{

    public static void main(String args[])
    {

        computeMD5Hash("dbox#service" + "2014-12-24T18:34:49");
    }

    public static void computeMD5Hash(String password)
    {
        try
        {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");

            digest.update(password.getBytes("UTF-16"));
            digest.update(password.getBytes());

            byte messageDigest[] = digest.digest();
            StringBuffer MD5Hash = new StringBuffer();

            for (int i = 0; i < messageDigest.length; i++)
            {
                String h = Integer.toHexString(0xFF & messageDigest[i]);

                while (h.length() < 2)
                    h = "0" + h;

                MD5Hash.append(h);
            }

            // result.setText("MD5 hash generated is: " + " " + MD5Hash);
            System.out.println("MD5 hash generated is: " + " " + MD5Hash);

        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
    }
}
