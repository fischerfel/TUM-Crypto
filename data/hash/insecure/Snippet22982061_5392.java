@RequestMapping(value = "/register", method = RequestMethod.POST)
@ResponseBody
public RegisterResponse newAccount(@RequestBody Register registration) {
    String newAccountSql = "INSERT INTO register (email,password,username) VALUES (:email,:password,:username)";
    RegisterResponse regResponse = new RegisterResponse();
    regResponse.setResult(-1);
    // ServiceDataBean<AuthToken> retBean = new
    // ServiceDataBean<AuthToken>();
    try {
        System.out.println("register service calling.....");
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("email", registration.getEmail());
        messageDigest = MessageDigest.getInstance("MD5");
        byte[] md5 = new byte[64];
        messageDigest.update(
                registration.getPassword().getBytes("iso-8859-1"), 0,
                registration.getPassword().length());
        md5 = messageDigest.digest();
        namedParameters.addValue("password", convertedToHex(md5));
        namedParameters.addValue("username", registration.getUsername());
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        // TODO what to do with the updInt also check it's not -1
        int updInt = jdbcTemplate.update(newAccountSql, namedParameters,
                generatedKeyHolder);
        regResponse.setResult(0);
        System.out.println("from register");
    } catch (Throwable e) {
        regResponse.setResult(001);
        e.printStackTrace();
    }
    return regResponse;
}
