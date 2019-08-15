 public static String decrypt (String data, String stringKey) throws Exception {
        JSONObject jsonKey = new JSONObject(stringKey);

        BigInteger n = new BigInteger(Base64.decode(jsonKey.getString("n"), Base64.DEFAULT));
        BigInteger e = new BigInteger("10001");
        BigInteger d = new BigInteger(Base64.decode(jsonKey.getString("d"), Base64.DEFAULT));
        BigInteger p = new BigInteger(Base64.decode(jsonKey.getString("p"), Base64.DEFAULT));
        BigInteger q = new BigInteger(Base64.decode(jsonKey.getString("q"), Base64.DEFAULT));
        BigInteger dmp1 = new BigInteger(Base64.decode(jsonKey.getString("dmp1"), Base64.DEFAULT));
        BigInteger dmq1 = new BigInteger(Base64.decode(jsonKey.getString("dmq1"), Base64.DEFAULT));
        BigInteger coeff = new BigInteger(Base64.decode(jsonKey.getString("coeff"), Base64.DEFAULT));


        KeySpec privateKeySpec = new RSAPrivateCrtKeySpec(n, e, d, p, q, dmp1, dmq1, coeff);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] dec = cipher.doFinal(data.getBytes());
        return new String(Base64.decode(dec, Base64.DEFAULT));
    }
