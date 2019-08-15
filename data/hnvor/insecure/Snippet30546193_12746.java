    LdapContextSource lcs = new LdapContextSource();

    lcs.setBase("[base]");
    lcs.setUserDn("[userDn]");
    lcs.setPassword("[password]");
    lcs.setPooled(false);
    lcs.setUrl("ldaps://[server-address]:636");

    DefaultTlsDirContextAuthenticationStrategy strategy = new DefaultTlsDirContextAuthenticationStrategy();
    strategy.setShutdownTlsGracefully(true);
    strategy.setSslSocketFactory(new CustomSSLSocketFactory());  // <-- not considered at all
    strategy.setHostnameVerifier(new HostnameVerifier(){

        @Override
        public boolean verify(String hostname, SSLSession session){

            return true;
        }
    });

    lcs.setAuthenticationStrategy(strategy);
    lcs.afterPropertiesSet();
    lcs.getContext("[principal]", "[credential]");
