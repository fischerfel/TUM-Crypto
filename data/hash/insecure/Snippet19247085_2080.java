    public void computeSHAHash(String path)// path to your file
    {
            String SHAHash = null;
    try 
    {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        InputStream in = new FileInputStream(path);
        byte[] buf = new byte[8192];
        int len = -1;
        while((len = in.read(buf)) > 0) 
        {
            md.update(buf, 0, len);
        }
        in.close();
        byte[] data = md.digest();
        try 
        {
           SHAHash = convertToHex(data);
        } 
        catch (IOException e) 
        {
           // TODO Auto-generated catch block
           e.printStackTrace();
        }
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
      Toast.makeToast(getApplicationContext(),"Generated Hash ="+SHAHash,Toast.LENGTH_SHORT).show();  

   }
 private static String convertToHex(byte[] data) throws java.io.IOException
{
    StringBuffer sb = new StringBuffer();
    String hex = null;

    hex = Base64.encodeToString(data, 0, data.length, NO_OPTIONS);

    sb.append(hex);

    return sb.toString();
}
