/**
 * 获取签名值
 *
 * @param secretKey
 * @param params
 *            请求参数
 * @return
 */
private static String getSign(String secretKey, Map<String, String[]> params) {

    String[] keys = params.keySet().toArray(new String[0]);
    Arrays.sort(keys);

    StringBuilder query = new StringBuilder();
    for (String key : keys) {
        if (!"sign".equals(key)) {
            String[] value = params.get(key);
            query.append(key);
            for (String string : value) {
                query.append(string);
            }
        }

    }
    return MD5(secretKey + query.toString());
}

public static String MD5(String str) {

    try {
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.reset();
        m.update(str.getBytes("UTF-8"));
        BigInteger bigInt = new BigInteger(1, m.digest());
        String hashtext = bigInt.toString(16);

        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;

    } catch (Exception e) {
        throw new RuntimeException("MD5 init failed.", e);
    }
}
