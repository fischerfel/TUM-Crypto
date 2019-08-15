private static void testing()
{
  try
  {
    // Unremark these lines to see it work
    //Security.addProvider(new BouncyCastleProvider()); // "BC"
    //Cipher oCIPH=Cipher.getInstance("AES/GCM/NoPadding", "BC");

    // Unremark these lines to see it fail
    Cipher oCIPH=Cipher.getInstance("AES/GCM/NoPadding", "SunJCE"); 

    // Make a quick and dirty IV and Symmetric Key
    byte[] baIV="EECE34808EF2A9AC".getBytes("UTF-8");
    byte[] baKey="010F05E3E0104EB59D10F37EA8D4BB6B".getBytes("UTF-8");

    // Make IV and Key (well KeySpec for AES) object. Use IV parspec because
    // defaults to 128bit Authentication tag size & works in both GCM & CBC.
    IvParameterSpec ps=new IvParameterSpec(baIV);
    SecretKeySpec sk=new SecretKeySpec(baKey,"AES");

    // Unremakr one line, either shrtline (multiple of 16 bytes) or long line   
    //String sPlainText="The non-encrypted (AES) message.";
    String sPlainText="The non-encrypted (AES) message. Everything after the . makes this NOT a multiple of 16 bytes.";

    // Encrypt
    oCIPH.init(Cipher.ENCRYPT_MODE, sk, ps);
    byte[] baEncrypted=oCIPH.doFinal(sPlainText.getBytes());

    // Decrypt
    oCIPH.init(Cipher.DECRYPT_MODE, sk, ps);
    String sDecryptedText=new String(oCIPH.doFinal(baEncrypted),"UTF-8");        
  }
  catch(Exception e)
  {
    MetaLogbook.log("Security Tools Exception",e);
  }
} 
