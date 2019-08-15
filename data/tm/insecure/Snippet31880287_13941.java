TrustManager[] trustAllCerts = new TrustManager[]{
                  new X509TrustManager() {

                      public java.security.cert.X509Certificate[] getAcceptedIssuers()
                      {
                          return null;
                      }
                      public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                      {
                          //No need to implement.
                      }
                      public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                      {
                          //No need to implement.
                      }
                  }
          };


          LdapConnectionConfig connectionConfig = new
                   LdapConnectionConfig();
                               connectionConfig.setLdapHost("XXX");
                               connectionConfig.setLdapPort(636);
                               connectionConfig.setName("XXX");
                               connectionConfig.setCredentials("XXX");
                               connectionConfig.setUseSsl(true);
                               connectionConfig.setSslProtocol("SSLv3");
                            //   connectionConfig.setTrustManagers(trustAllCerts);

                              connection = new LdapNetworkConnection(connectionConfig);


                              connection.bind();
