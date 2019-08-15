if(useSSL)
{
  try 
  {   
    SSLContext se = SSLContext.getInstance("TLS");
    Security.addProvider(se.getProvider());
  }   
  catch(NoSuchAlgorithmException e) { }

  System.setProperty("javax.net.ssl.trustStore", System.getProperty("java.home") + "/lib/security/cacerts");

  com.org.ldap.LDAPSocketFactory ssf = new LDAPJSSESecureSocketFactory();
  LDAPConnection.setSocketFactory(ssf);
}

try 
{   
  lc = new LDAPConnection();
  lc.connect( ldapServer, ldapPort);
  lc.bind( ldapVersion,  ldapUser, (userInfo[1]).getBytes() );
}
catch (LDAPException le)
{
  le.printStackTrace();
}
