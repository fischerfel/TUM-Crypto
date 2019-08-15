public  String pwdEncodePBKDF2(String unencryptedPassword,String salt)
{
try
{
        if(salt.isEmpty())
        {
            salt = generateSalt(SystemSecurity.SALTLENGTH);
        }
        String algorithm = "PBKDF2WithHmacSHA1";
        int derivedKeyLength = 160;
        int iterations = 1000;
        KeySpec spec = new PBEKeySpec(unencryptedPassword.toCharArray(), salt.getBytes(), iterations, derivedKeyLength);
        SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);
        StringBuffer hexString = new StringBuffer();
        byte[] mdbytes  =  f.generateSecret(spec).getEncoded();
        for (int i=0;i<mdbytes.length;i++)
        {
            hexString.append(Integer.toHexString(0xFF & mdbytes[i]));
        }
        String hashedPassword = hexString.toString();
        return hashedPassword  + salt;
    }
    catch(Exception e)
    {
        e.printStackTrace();
        throw new RuntimeException("Error computing hash: "+e.getMessage());
    }        
}
