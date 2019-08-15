public String createHashString(String s)
{

    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytesOfMessage = s.getBytes("UTF-8");
        byte[] thedigest = md.digest(bytesOfMessage);


        String hexString = "";
        for(byte bi : thedigest)
        {
            String hex = Integer.toHexString(0xFF & bi);
            if (hex.length() == 1) {

                hexString += "0";
            }
            hexString += (hex);
        }

        return hexString;

    } 
    catch (Exception e) {
        return "";
    }
}
