String stringToSign = "GET\n" 
       + "\n" // content encoding
       + "\n" // content language
       + "\n" // content length
       + "\n" // content md5
       + "\n" // content type
       + "\n" // date
       + "\n" // if modified since
       + "\n" // if match
       + "\n" // if none match
       + "\n" // if unmodified since
       + "\n" // range
       + "x-ms-date:" + date + "\nx-ms-version:2015-02-21\n" // headers
       + "/" + <your account name> + "/"+"\ncomp:list"; // resources
       String auth = getAuthenticationString(stringToSign);

private static String getAuthenticationString(String stringToSign) throws Exception {
       Mac mac = Mac.getInstance("HmacSHA256");
       mac.init(new SecretKeySpec(Base64.decode(key), "HmacSHA256"));
       String authKey = new String(Base64.encode(mac.doFinal(stringToSign.getBytes("UTF-8"))));
       String auth = "SharedKey " + account + ":" + authKey;
       return auth;
}
