private String checkSum(File file,String checksumAlgorithm) throws Exception 
{
    MessageDigest digest = MessageDigest.getInstance(checksumAlgorithm);
    InputStream input = null;
    input = new FileInputStream(file);
    byte[] buffer = new byte[8192];
    do {
        int read = input.read(buffer);
        if(read <= 0)
            break;
        digest.update(buffer, 0, read);
    } while(true);
    input.close();
    byte[] sum = digest.digest();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < sum.length; i++) {
        sb.append(Integer.toString((sum[i] & 0xff) + 0x100, 16).substring(1));
    }        
    return sb.toString();
}
