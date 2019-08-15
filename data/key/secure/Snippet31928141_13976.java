public static String ComputeHash(String[] dataArray, String privateKey) {

    String hash="";

    String data = "";

    for (String item : dataArray) {
        data += item;
    }

    try {

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(privateKey.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        //hash = Base64.encodeBase64String(sha256_HMAC.doFinal(data.getBytes()));
        //hash = Base64.encodeToString(sha256_HMAC.doFinal(data.getBytes("UTF-8")), Base64.DEFAULT);
        hash = new String(Base64.encodeBase64(sha256_HMAC.doFinal(data.getBytes("UTF-8"))));

    } catch (Exception e) {
        Toast.makeText(MyApplication.getAppContext(), "Error", Toast.LENGTH_LONG).show();
    }
    return hash;
}

public <S> S createService(Class<S> serviceClass){

    stamp = String.valueOf(System.currentTimeMillis() / 1000);//get the number of seconds since the epoch

    String[] data = new String[]{"thisisatestpublickey", 1438915015+"", "GET"};
    expectedSignature = ComputeHash(data, "thisistestprivatekey");

    RequestInterceptor requestInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade request) {
            request.addHeader("Authorization", "Basic YXZlbmdlcnM6bWlnaHR5bWluZHM=");
            request.addHeader(ApplicationConstants.PublicKeyHeaderName, "thisisatestpublickey");
            request.addHeader(ApplicationConstants.StampHeaderName, 1438915015+"");
            request.addHeader(ApplicationConstants.SignatureHeaderName, expectedSignature);

        }
    };


}
