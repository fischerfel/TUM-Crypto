 public void doSendJsonRequest(final ERequest ERequest) {
 requestMethod = String.valueOf(ERequest.method);
        requestUrl = String.valueOf(ERequest.mReqUrl);
        if(requestMethod.equals(Request.Method.GET)){
            requestMethod = "GET";
        }else if(requestMethod.equals(Request.Method.POST)){
            requestMethod = "POST";
        }else if(requestMethod.equals(Request.Method.PUT)){
            requestMethod = "PUT";
        }else if(requestMethod.equals(Request.Method.DELETE)){
            requestMethod = "DELETE";
        }

 Long tsLong = System.currentTimeMillis()/1000;
        final  String ts = tsLong.toString();

      final String  kk = requestMethod+"&" + encode(requestUrl)+"&";
        final String  kk = encode("GET"+"&"
                + requestUrl+"&"
                + OAUTH_CONSUMER_KEY + "=\"4e77abaec9b6fcda9b11e89a9744c2e1\"&"
                +OAUTH_NONCE + "=\"" + getNonce()+ "\"&"
                +OAUTH_SIGNATURE_METHOD + "=\""+OAUTH_SIGNATURE_METHOD_VALUE+"\"&"
                +OAUTH_TIMESTAMP + "=\"" + ts + "\"&"
                +OAUTH_TOKEN +"=\"2da943934104293b167fe2feaffca9a1\"");


        RequestQueue queue = VolleyUtils.getRequestQueue();
        try {
            JSONObject jsonObject = ERequest.jsonObject;


            EJsonRequest myReq = new EJsonRequest(ERequest.method, ERequest.mReqUrl, jsonObject, createReqSuccessListener(ERequest), createReqErrorListener(ERequest)) {

                public Map < String, String > getHeaders() throws AuthFailureError {
//                    Long tsLong = System.currentTimeMillis()/1000;
//                    String ts = tsLong.toString();
                    String strHmacSha1 = "";
                    String oauthStr = "";

                    strHmacSha1 = generateSignature(kk, oAuthConsumerSecret, oAuthTokenSecret);
                    strHmacSha1 = toSHA1(strHmacSha1.getBytes());

                    Log.e("SHA   !",strHmacSha1);


                     oauthStr ="OAuth "+ OAUTH_CONSUMER_KEY + "=\"4e77abaec9b6fcda9b11e89a9744c2e1\","
                            +OAUTH_TOKEN +"=\"2da943934104293b167fe2feaffca9a1\","
                            +OAUTH_SIGNATURE_METHOD + "=\""+OAUTH_SIGNATURE_METHOD_VALUE+"\","
                            +OAUTH_TIMESTAMP + "=\"" + ts + "\","
                            +OAUTH_NONCE + "=\"" + getNonce()+ "\","
                            +OAUTH_VERSION + "=\"" + OAUTH_VERSION_VALUE + "\","
                            +OAUTH_SIGNATURE + "=\"" + strHmacSha1+ "\"";

                    Log.e("VALUE OF OAuth str",oauthStr);


                    Map<String, String> params = new HashMap<String, String>();
                                 params.put("Content-Type", "application/json");
                                 params.put("Authorization",oauthStr);
                                // params.put("Authorization",getConsumer().toString());



                                 return params;

                }


            };

            myReq.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 4,
                    BABTAIN_MAX_RETRIES,
                    BABTAIN_BACKOFF_MULT));
                              myReq.setHeader("Cache-Control", "no-cache");
                             //myReq.setHeader("Content-Type", "application/json");
                                 queue.add(myReq);
        } catch (Exception e) {
            e.printStackTrace();
        }

 private String generateSignature(String signatueBaseStr, String oAuthConsumerSecret, String oAuthTokenSecret) {
        byte[] byteHMAC = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec spec;
            if (null == oAuthTokenSecret) {
                String signingKey = encode(oAuthConsumerSecret) + '&';
                spec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");
            } else {
                String signingKey = encode(oAuthConsumerSecret) + '&' + encode(oAuthTokenSecret);
                spec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");
            }
            mac.init(spec);
            byteHMAC = mac.doFinal(signatueBaseStr.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(byteHMAC, Base64.DEFAULT);
        return base64.trim();
    }

  private String toSHA1(byte[] convertme) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        }
        catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return byteArrayToHexString(md.digest(convertme));
    }

    private String byteArrayToHexString(byte[] b) {
        String result = "";
        for (int i=0; i < b.length; i++)
            result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        return result;
    }
