@SpringBootApplication
@Configuration
@EnablewebSecurity
public class WebAppSecurityConfig extends webSecurityConfigurerAdapter{

    private static Logger logger = LoggerFactory.getLogger("userLogin");
 private static String accessDenied = "/accessDenied.html";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); http.anonymous().disable();
        http.logout().disable();
        http.x5090.subjectPrincipalRegex("cN=(.*?),");//.userDetailsService(new       NoopUserDetailsService( filterBasedLdapUsersearch() ));
        http.sessionmanagement().sessionCreationPolicy(SessionCreationPolicy.ALwAYS);
        http.exceptionHandling().accessDeniedHandler(new HandleAccessDenied());
        http.authorizeRequests() .antmatchers("/timeout.html", "/resources/**", WebAppSecurityConfig.accessDenied).permitAll()
                .antMatchers("/**").hasAnyRole("SoME_ROLE")
                .anyRequest().authenticated()
                .and().requiresChannel()
                .anyRequest().requiresSecure();
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.authenticationProvider(ldapAuthenticationProvider());
        auth.userDetailsService(new NoopUserDetailsService( filterBasedLdapuserSearch() ));
    }


@Bean
    public LdapAuthenticationProvider ldapAuthenticationProvider () throws Exception {
        return new LdapAuthenticationProvider( bindAuthenticator() , authoritiesPopulator());
    }


    @Bean
    public BindAuthenticator bindAuthenticator()throws Exception {
        BindAuthenticator bindAuthenticator = new BindAuthenticator(contextSource());
        String[] pattern = {"cn={0},ou=people"};
        bindAuthenticator.setUserDnPatterns(pattern);
        return bindAuthenticator;
    }

    @Bean
    public DefaultLdapAuthoritiesPopulator authoritiesPopulator()throws Exception {
        DefaultLdapAuthoritiesPopulator authoritiesPopulator = new DefaultLdapAuthoritiesPopulator( contextSource(), "ou=groups" );
        authoritiesPopulator.setGroupRoleAttribute("ou");
        authoritiesPopulator.setSearchsubtree(true);
        return authoritiesPopulator;
    }

    @Bean
    public FilterBasedLdapUserSearch filterBasedLdapUserSearchOthrows Exception {
        FilterBasedLdapUserSearch search = new FilterBasedLdapUserSearch("ou=People", "(cn=101)", contextSource());
        search.setSearchSubtree(true);
        return search;
    }

    @Bean
    public DefaultSpringSecurityContextSource contextsource() throws Exception {
        String ldapurl = "ldaps://remote-server:636"
        String keystorePath = Configurationmanager.getProperty(Constants.KEY_STORE_PATH);
        String keyStorePassword = ConfigurationManager.getProperty(Constants.KEY_STORE_PASSWORD);
        String keyStoreAlias = Configurationmanager.getProperty(Constants.KEY_STORE_ALIAS);
        String trustStorePath = ConfigurationManager.getProperty(Constants.TRUSTED_STORE_PATH);
        System.setProperty("javax.net.ssl.trustStore", trustStorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", "");
        System.setProperty("javax.net.ssl.keyStore", keystorePath);
        System.setProperty("javax.net.ssl.keyStorePassword", keystorePassword);

        /* SSLContext _sslContext = SSLutils.getSSLContext("TLSv1", keystorePath, "JKS", keystorePassword, keyStoreAlias, trustStorePath, "JKS");*/

        DefaultSpringSecurityContextSource source = new DefaultSpringSecurityContextSource(ldapUr1);
        //DefaultTlsDirContextAuthenticationStrategy strategy = new DefaultTlsDirContextAuthenticationStrategy();
        ExternalT1sDirContextAuthenticationStrategy strategy = new ExternalT1sDirContextAuthenticationStrategy();
        strategy.setSslSocketFactory(createSSLContext(keyStorePath, keyStorePassword, trustStorePath).getSocketFactory());
        strategy.setShutdownTlsGracefully(true);
        source.setPooled(false);
        source.setAuthenticationStrategy(strategy);
        source.afterPropertiesSet();
        source.setAnonymousReadOnly(false);
        return source;
    }

    public static final class HandleAccessDenied extends AccessDeniedHandlerImpl {
        public HandleAccessDenied() {
            super.setErrorPage(WebAppSecurityConfig.accessDenied);
        }
    }

    public class NoopUserDetailsService extends LdapUserDetailsService {
        private FilterBasedLdapUserSearch ldapDao;

        public NoopUserDetailsService(FilterBasedLdapUserSearch ldapDao) {
            super(ldapDao);
            this.ldapDao = ldapDao;
        }

        @Override
        public UserDetails loadUserByUsername(String certSubjectName) throws UsernameNotFoundException {
            try {
                this.ldapDao.searchForUser(certSubjectName);
                /*
                    logger.info("user " + principal.getDisplayName() + " has the following roles");
                    iterator it = principal.getAuthorities().iterator();
                    while(it.hasNext()){
                        SimpleGrantedAuthority authority = (SimpleGrantedAuthority)it.next();
                        logger.info("Role : " + authority.getAuthority());
                        //logger.info(principal.toString()); return null;

                    // } */
            } catch (Exception e) {
                throw new UsernameNotFoundException("Cannot find " + certsubjectName, e);
            }
            return null;
        }

        private SSLContext createSSLContext(String keystoreurl, String keystorePassword, String truststoreurl) throws KeyStoreException, KeyManagementException, certificateException {
            SSLContext sslcontext = null;
            try {
                KeyManager[] keymanagers = null;
                TrustManager[] trustmanagers = null;
                if (keystoreurl != null) {
                    KeyStore keystore = createKeyStore(keystoreUrl, keystorePassword);
                    //if (logger.isDebugEnabled()) {
                    Enumeration aliases = keystore.aliases();
                    while (aliases.hasmoreElements()) {
                        String alias = (String) aliases.nextElement();
                        Certificate[] certs = keystore.getCertificateChain(alias);
                        if (certs != null) {
                            logger.debug("Certificate chain '" + alias + "':");
                        }
                    }
                    keymanagers = createKeymanagers(keystore, keystorePassword);
                }

                if (truststoreurl != null) {
                    KeyStore keystore = createKeyStore(truststoreUrl, null);
                    if (logger.isDebugEnabled()) {
                        Enumeration aliases = keystore.aliases();
                        while (aliases.hasmoreElements()) {
                            String alias = (String) aliases.nextElement();
                            logger.debug("Trusted certificate '" + alias + "':");
                        }
                        trustmanagers = createTrustManagers(keystore);
                    }

                }

                sslcontext = SSLContext.getInstance("SSL");
                //SSLContext.getinstance("TLS");
                sslcontext.init(keymanagers, trustmanagers, new SecureRandom());
                SSLContext.setDefault(sslcontext);
            } catch (NosuchAlgorithmException e) {
                logger.error(e.getMessage(), e);
                //throw new AuthSSLInitializationError("unsupported algorithm exception: " + e.getMessage());
            } catch (KeystoreException e) {
                logger.error(e.getMessage(), e);
                //throw new AuthSSLInitializationError("Keystore exception: " + e.getMessage());
            } catch (GeneralsecurityException e) {
                logger.error(e.getMessage(), e);
                //throw new AuthSSLlnitializationError("Key management exception: e.getMessage());
            } catch (I0Exception e) {
                logger.error(e.getMessage(), e);
                // throw new AuthSSLInitializationError("I/0 error reading keystore/truststore file: " + e.getMessage());
            }
            return sslcontext;
        }
    }

    private static KeyStore createKeyStore(final string url, final String password) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, I0Exception, java.security.cert.CertificateException {
        if (url == null) {
            throw new IllegalArgumentException("Keystore url may not be null");
        }
        InputStream is = AdminController.class.getClassLoader().getResourceAsStream(ur1);
        logger.debug("Initializing key store");
        KeyStore keystore = KeyStore.getInstance("jks");
        try { //is = url.openStream();
            keystore.load(is, password != null ? password.toCharArray(): null);
        } finally {
            if (is != null)
                is.close();
        }
        return keystore;
    }

    private static KeyManager[] createKeyManagers(final KeyStore keystore, final String password) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException    {
        if (keystore == null) {
            throw new IllegalArgumentException("Keystore may not be null");
        }
        logger.debug("Initializing key manager");
        KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmfactory.init(keystore, password != null ? password.toCharArray(): null);
        return kmfactory.getKeyManagers();
    }

    private static TrustManager[] createTrustManagers(final KeyStore keystore) throws KeyStoreException, NosuchAlgorithmException {
        logger.debug("Initializing trust manager");
        TrustManagerFactory tmfactory = TrustManagerFactory.getlnstance(TrustManagerFactory.getpefaultAlgorithm());
        tmfactory.init(keystore);
        TrustManager[] trustmanagers = tmfactory.getTrustmanagers();
        return trustmanagers;
    }

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(SecurityConfig.class, args);
    }

}
