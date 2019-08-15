private static String generateMD5(FileInputStream inputStream){
    if(inputStream==null){

        return null;
    }
    MessageDigest md;
    try {
        md = MessageDigest.getInstance("MD5");
        FileChannel channel = inputStream.getChannel();
        ByteBuffer buff = ByteBuffer.allocate(2048);
        while(channel.read(buff) != -1)
        {
            buff.flip();
            md.update(buff);
            buff.clear();
        }
        byte[] hashValue = md.digest();
        return new String(hashValue);
    }
    catch (NoSuchAlgorithmException e)
    {
        return null;
    } 
    catch (IOException e) 
    {
        return null;
    }
    finally
    {
        try {
            if(inputStream!=null)inputStream.close();
        } catch (IOException e) {

        }
    } 
}
