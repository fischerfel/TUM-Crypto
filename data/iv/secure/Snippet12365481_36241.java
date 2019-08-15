public void decrypt()
{
try
{
    SecretKeySpec sk=new SecretKeySpec(rsa_priv,"RSA/EBC/PKCS8");
    Cipher dec = Cipher.getInstance("RSA");
    dec.init(Cipher.DECRYPT_MODE, sk,new IvParameterSpec(iv));
     //OAEPWithSHA-512AndMGF1Padding        
     byte temp[];
     temp=dec.doFinal(sess);
     String t=temp.toString();
     System.out.println("Session key is:"+ t);
     //session=dec(sess,rsa_priv);OAEPWithSHA-256AndMGF1Padding
}
catch (Exception e)
{
    System.out.println("Exception occured:"+ e);
}
}
