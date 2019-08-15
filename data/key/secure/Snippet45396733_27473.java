//Sample method to construct a JWT
public static String createJWT(String id, String issuer, String subject, 
        String payload, String role, String name, long ttlMillis) {
    String jwt = "";

    try {
        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(apiKey.getSecret());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                                    .setIssuedAt(now)
                                    .setSubject("test")
                                    .claim("role", role)
                                    .claim("name", name)
                                    .setIssuer("tester")
                                    .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, signingKey);
                                    //.signWith(signatureAlgorithm, apiKey.getSecret().getBytes("UTF-8"));
                                    //

        //if it has been specified, let's add the expiration
        if (ttlMillis >= 0) {
        long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        jwt = builder.compact();

    } catch (Exception ex) {

    }
    return jwt;
}
