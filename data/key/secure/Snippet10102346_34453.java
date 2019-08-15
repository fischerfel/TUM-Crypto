private String hmacSHA256(String data) throws Exception {
    String key = sharedPrefs.getString(Constants.SECRET, "error");
    SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(secretKey);
    byte[] hmacData = mac.doFinal(data.getBytes("UTF-8"));
    //Log.i(TAG, "BYTE ARRAY TO STRING: " + Base64.encodeToString(hmacData, Base64.DEFAULT));
    String value = Base64.encodeToString(hmacData, Base64.DEFAULT);
    return value;
}
