String md5(String s)
{
        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
        digest.update(s.getBytes());
        byte messageDigest[] = digest.digest();

        // Create Hex String
        StringBuffer hexString = new StringBuffer();
        for (int i=0; i<messageDigest.length; i++)
        hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

        return hexString.toString();
}

String md5hash = md5("a test message");
