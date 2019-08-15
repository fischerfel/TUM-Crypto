  try 
  {   
    SSLContext se = SSLContext.getInstance("TLS");
    Security.addProvider(se.getProvider());
  }   
  catch(NoSuchAlgorithmException e) { }
