    public String sign(Map<String, String> params) {
        params.put("AWSAccessKeyId", this.awsAccessKeyId);
        params.put("Timestamp", this.timestamp());
        SortedMap<String, String> sortedParamMap = new TreeMap<String, String>(params);
        // get the canonical form the query string
        String canonicalQS = this.canonicalize(sortedParamMap);

        // create the string upon which the signature is calculated 
        String requestPayload = "";
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
           md.update("".getBytes("UTF-8"));
           byte[] digest = md.digest();
           requestPayload = Hex.encodeHexString(digest);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        String canonicalRequest = 
                REQUEST_METHOD + "\n" 
                + REQUEST_URI + "\n"
                + canonicalQS + "\n"
                + "host:"+ this.endpoint + "\n"
                + "x-amz-date:"+timestamp() + "\n"
                + "host;x-amz-date" + "\n"
                + requestPayload;
        String tempDate = (new SimpleDateFormat("yyyyMMdd").format(new Date())).toString();
        String signedString = "";

        String toSign = 
                "AWS4-HMAC-SHA256" + "\n"
                + toSignTimestamp() + "\n"
                + signDate()+"/us-east-1/iam/aws4_request"+ "\n";

        System.out.println("toSign Before Sign\n"+toSign);
        try{
           MessageDigest md = MessageDigest.getInstance("SHA-256");
           md.update(canonicalRequest.getBytes("UTF-8"));
           byte[] digestCanonical = md.digest();
           toSign+=Hex.encodeHexString(digestCanonical);
           System.out.println("toSign after Sign\n"+toSign);


           byte[] kSigning = getSignatureKey(awsSecretKey, tempDate, "us-east-1", "onca");
           byte[] byteSigned = HmacSHA256(kSigning, toSign);
           signedString = Hex.encodeHexString(byteSigned);

       }
       catch(Exception e){
           e.printStackTrace();
       }
                String url = "http://" + this.endpoint + REQUEST_URI + "?" + canonicalQS + "&Signature="+signedString;

        System.out.println(url);
        return url;
    }
