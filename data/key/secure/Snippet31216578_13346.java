private static String generateSasToken(String uri, String keyName, String key){
        String ret = "";

       // long tokenExpirationTime = (System.currentTimeMillis() / 1000) + (10 * 365 * 24 * 60 * 60);

        Date now = new Date();
        Date previousDate=new Date(1970);
        long tokenExpirationTime = ((now.getTime() - previousDate.getTime()) / 1000 )+3600;

        try {
            String stringToSign = URLEncoder.encode(new URL(uri).toString(),java.nio.charset.StandardCharsets.UTF_8.toString()) + "\n" + tokenExpirationTime;

            System.out.println(stringToSign);
            SecretKey secretKey = null;

            byte[] keyBytes = key.getBytes("UTF-8");

            Mac mac = Mac.getInstance("HMACSHA256");

            secretKey = new SecretKeySpec(keyBytes, mac.getAlgorithm());

            mac.init(secretKey);

            byte[] digest = mac.doFinal(stringToSign.getBytes());
            //We then use the composite signing key to create an oauth_signature from the signature base string
            String signature = Base64.encodeBase64String(digest);
            System.out.println( URLEncoder.encode(signature, java.nio.charset.StandardCharsets.UTF_8.toString()));
           // String signature = Base64.encodeBase64String(mac.doFinal(stringToSign.getBytes("UTF-8")));
            ret = String.format("SharedAccessSignature sr=%s&sig=%s&se=%s&skn=%s",
                    URLEncoder.encode(uri, java.nio.charset.StandardCharsets.UTF_8.toString()),
                    URLEncoder.encode(signature, java.nio.charset.StandardCharsets.UTF_8.toString()),
                    String.valueOf(tokenExpirationTime),
                    keyName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return ret;
    }
