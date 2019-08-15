private NamedParameterJdbcTemplate jdbcTemplate;

@Autowired
public void setDataSource(DataSource dataSource) {
    jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    System.out.println("--------------"+jdbcTemplate.toString());
}

private static Map<String, AuthToken> tokenHash = new ConcurrentHashMap<String, AuthToken>();

private static String authTokenDetailsSql = "select * from authtoken where token = :token";

@Override
@RequestMapping(value = "/register", method = RequestMethod.POST)
@ResponseBody
public ServiceBean newAccount(@RequestBody Registration registration) {
    String newAccountSql = "INSERT INTO account (email,password,name) VALUES (:email,:password,:name)";
    ServiceDataBean<AuthToken> retBean = new ServiceDataBean<AuthToken>();
    try {
        System.out.println("register service calling.....");
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("email", registration.getEmail());
        messageDigest = MessageDigest.getInstance("MD5");
        byte[] md5 = new byte[64];
        messageDigest.update(registration.getPassword().getBytes("iso-8859-1"), 0, registration.getPassword().length());
        md5 = messageDigest.digest();
        namedParameters.addValue("password", convertedToHex(md5));
        namedParameters.addValue("name", registration.getName());
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        // TODO what to do with the updInt also check it's not -1
        int updInt = jdbcTemplate.update(newAccountSql, namedParameters, generatedKeyHolder);
        long accountId = (Long) generatedKeyHolder.getKeys().get("GENERATED_KEY");
        registration.getDevice().setOwner(registration.getId());
        fotoframz.register(registration.getDevice());
        Login login = new Login();
        login.setEmail(registration.getEmail());
        login.setPassword(registration.getPassword());
        login.setDevice(registration.getDevice());
        retBean = (ServiceDataBean<AuthToken>) this.login(login);
        System.out.println("form register");
    } catch (Throwable e) {
        retBean.setStatusCode("001");
        e.printStackTrace();
    }
    return retBean;
}
