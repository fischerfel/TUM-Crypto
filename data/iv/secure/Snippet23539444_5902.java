    public String decryptJson(String data)
{
    String[] split= data.trim().split("=");

    byte[] iv = Base64.decode(split[0],3);      
    String hash = split[1];
    byte[] encd = Base64.decode(split[2],0);

    String skey  = "secretkeyfromdatabase";
    byte[] skeyb = skey.getBytes();

            try
            {                   
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            SecretKeySpec skeyspec = new SecretKeySpec(skeyb,"AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            cipher.init(Cipher.DECRYPT_MODE,skeyspec,ivspec);

            byte[] original = cipher.doFinal(encd);

            return original.toString();

            }
            catch(Exception ex)
            {
                ex.printStackTrace();
                return "ERROR! | "+ex+"IV:"+iv;
            }
}
