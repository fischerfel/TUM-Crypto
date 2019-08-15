private String hash_hmac_test(String base_string, String key) throws Exception {

    try {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret = new SecretKeySpec(key.getBytes("UTF-8"), mac.getAlgorithm());
        mac.init(secret);

        String base_string_base64 = Base64.encodeToString(base_string.getBytes(), Base64.NO_WRAP);

        byte[] digest = mac.doFinal(base_string_base64.getBytes());
        //android.util.Base64.encodeT
        String retVal = Base64.encodeToString(digest,Base64.DEFAULT);

        Log.v(TAG, "String: " + base_string);


        Log.v(TAG, "String in Base64: " + base_string_base64);

        Log.v(TAG, "key: " + key);
        Log.v(TAG, "result: " + retVal);
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }

    return "ok";
}
