private String getHash(String input) 
{
    String ret = null;
    try 
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        byte[] bs = md.digest(input.getBytes("US-ASCII"));


        StringBuffer sb = new StringBuffer();
        for (byte b : bs) 
        {
            String bt = Integer.toHexString(b & 0xff);
            if(bt.length()==1) 
            {
                sb.append("0");
            }
            sb.append(bt);
        }
        ret = sb.toString();
    } 
    catch (Exception e) 
    {
    }
    return ret;
}
