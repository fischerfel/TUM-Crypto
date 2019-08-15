String baseString="POST&";
            String subBaseString = "oauth_consumer_key="+oauth_consumer_key+"&oauth_nonce="+nonce+"&oauth_signature_method="+oauth_signature_method;
            subBaseString += "&oauth_timestamp="+  oauth_timestamp+"&oauth_token="+oauth_token+"&oauth_version=1.0";
            baseString+=URLEncoder.encode(baseRequest, "UTF-8");
            baseString += "&" +  URLEncoder.encode(subBaseString, "UTF-8");

            String result;
            try {

            SecretKeySpec signingKey = new SecretKeySpec(oauth_consumer_key.getBytes(), oauth_signature_method);

            Mac mac = Mac.getInstance(oauth_signature_method);
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(baseString.getBytes());

            // base64-encode the hmac
            result = Base64.encode(rawHmac);

            } catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
            }
