 private static String checkSumInStream(String Str, String checksumAlgorithm) throws Exception
{           
    InputStream stream = new ByteArrayInputStream(Str.getBytes());
    MessageDigest digest = MessageDigest.getInstance(checksumAlgorithm);

    InputStream input = null;
    StringBuffer sb = new StringBuffer();
    try{
        input = stream;
        byte[] buffer = new byte[8192];
        do {
            int read = input.read(buffer);
            if(read <= 0)
                break;
            digest.update(buffer, 0, read);
        } while(true);
        byte[] sum = digest.digest();

        for (int i = 0; i < sum.length; i++) {
            sb.append(Integer.toString((sum[i] & 0xff) + 0x100, 16).substring(1));
        }

    }catch(IOException io)
    {

    }finally{
        if(input != null)
            input.close();
    }

    return sb.toString();
}
