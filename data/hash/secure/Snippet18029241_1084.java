String _Pass = new String(tfuserpass.getPassword());      
  //===========================================================================================================================================================
  MessageDigest md = null;   
    try
    {
        md = MessageDigest.getInstance("SHA-512");          
    } 
    catch (NoSuchAlgorithmException e)
    {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
  md.update(_Pass.getBytes());
  byte byteData[] = md.digest();

  StringBuffer sb = new StringBuffer();
  for (int i = 0; i < byteData.length; i++)
  {
   sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
  }


Class main()
{

//I have used MessageDigest to hash + salt in this class for password.
}

Class main1()
{
//Now I want to decode and compare the values with the actual value to authenticate the user here.


}
