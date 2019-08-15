public static String signRequest(String key, String data) throws Exception {
    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
    SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
    sha256_HMAC.init(secret_key);
    return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
}

public static void getLatestImagesForHashtag(String hashtag, String client_id, String secret) throws Exception {
    String endpoint = "/tags/" + URLEncoder.encode(hashtag, "UTF-8") + "/media/recent";
    String request_data = endpoint + "|client_id=" + client_id;
    System.out.println("SIGNING DATA: " + request_data);
    String signature = InstagramUtil.signRequest(secret, request_data);
    System.out.println("SIG VAL: " + signature);
    String url = "https://api.instagram.com/v1" + endpoint + "?client_id=" + client_id + "&sig=" + signature;
    URL obj;
    try {
        obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        //Blah blah blah
