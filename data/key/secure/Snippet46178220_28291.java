public static void myAttempt() throws Exception {

        String policy_document = constructPolicy();
        String aws_secret_key="REMOVED FOR SAFETY";

        String policy = (new BASE64Encoder()).encode(
                policy_document.getBytes("UTF-8")).replaceAll("\n","").replaceAll("\r","");

        String dateStamp ="20170912";
        String region = "eu-central-1";
        String serviceName ="s3";
        System.out.println("NEW SIGNATURE: "+getSignature(getSignatureKey(aws_secret_key,dateStamp,region,serviceName)));

        System.out.println("ENCODED POLICY: "+policy);        
    }

private static String constructPolicy() throws UnsupportedEncodingException {

        String policy_document="{\"expiration\": \"2018-01-01T00:00:00Z\",\n" +
                "  \"conditions\": [ \n" +
                "    {\"bucket\": \"testbucket-10.09.2017\"}, \n" +
                "    [\"starts-with\", \"$key\", \"uploads/\"],\n" +
                "    {\"acl\": \"private\"},\n" +
                "    {\"success_action_redirect\": \"http://localhost/\"},\n" +
                "    [\"starts-with\", \"$Content-Type\", \"\"],\n" +
                "    [\"content-length-range\", 0, 1048576]\n" +
                "  ]\n" +
                "}";

        String policy = (new BASE64Encoder()).encode(
                policy_document.getBytes("UTF-8")).replaceAll("\n","").replaceAll("\r","");
        return policy;
    }

private static byte[] HmacSHA256(String data, byte[] key) throws Exception {
        String algorithm="HmacSHA256";
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(data.getBytes("UTF8"));
    }

private static byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) throws Exception  {
        byte[] kSecret = ("AWS4" + key).getBytes("UTF8");
        byte[] kDate    = HmacSHA256(dateStamp, kSecret);
        byte[] kRegion  = HmacSHA256(regionName, kDate);
        byte[] kService = HmacSHA256(serviceName, kRegion);
        byte[] kSigning = HmacSHA256("aws4_request", kService);
        return kSigning;
    }

private static String getSignature(byte[] key) throws Exception{

        return base16().lowerCase().encode(HmacSHA256(constructPolicy(), key));
    }
