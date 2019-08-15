LdapNetworkConnection connection = null;
    LdapConnectionConfig ldapConnectionConfig = new LdapConnectionConfig();
    ldapConnectionConfig.setUseTls(true);
    ldapConnectionConfig.setLdapHost("localhost");
    ldapConnectionConfig.setLdapPort(10636);
    ldapConnectionConfig.setTrustManagers(new X509TrustManager(){
          public X509Certificate[] getAcceptedIssuers(){
              return new X509Certificate[0];
          }
          public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException{}
          public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException{}
    });

    connection = new LdapNetworkConnection(ldapConnectionConfig);
    connection.connect();

    try{
        connection.startTls();
    }catch(LdapException e){
        e.printStackTrace();
    }
