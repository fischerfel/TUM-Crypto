    OAuthParameters oauth;

    public OAuthParameters authChecking() {
        oauth = new OAuthParameters();
        GenericUrl genericUrl = new GenericUrl("http://localhost/wordpress/wc-api/v3/products/count");

        oauth.consumerKey = "ck_xxxxxxxxxxxxxxxxxxxxxxxxxxx";
        oauth.signatureMethod = "HMAC-SHA1";
        oauth.version = "3.0";
        oauth.computeTimestamp();
        oauth.computeNonce();

        oauth.signer = new OAuthSigner() {
            @Override
            public String getSignatureMethod() {

                return oauth.signatureMethod;
            }

            @Override
            public String computeSignature(String signatureBaseString) throws GeneralSecurityException {

                String key = "cs_xxxxxxxxxxxxxxxxxxxxxxxxxx";

                Mac mac = Mac.getInstance(
                        "HmacSHA1");
                SecretKeySpec secret = new SecretKeySpec(key.getBytes(), "HmacSHA1");

                mac.init(secret);
                byte[] digest = mac.doFinal(signatureBaseString.getBytes());
                Log.e("SIGNATURE Base64", new String(Base64.encode(digest, 0)).trim());

                String signature = new String(com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64.encodeBase64String(digest));
                return signature;
            }
        };
        try {
            oauth.computeSignature("GET", genericUrl);

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
        methodSignatureTest();
        return oauth;
    }


 @Override
    public void requestAPI(Object... param) {
        OAuthParameters oauth = authChecking();
        if (oauth != null) {
            String url = null;
            try {

                Toast.makeText(MainActivity.this, "Signature retrive called", Toast.LENGTH_SHORT).show();
                url = "http://localhost/wordpress/wc-api/v3/products/"+"count?oauth_consumer_key=" + oauth.consumerKey + "&oauth_signature_method=" + oauth.signatureMethod + "&oauth_timestamp=" + oauth.timestamp + "&oauth_nonce=" + oauth.nonce + "&oauth_version=" + oauth.version + "&oauth_signature="
//               + java.net.URLDecoder.decode(oauth.signature, "UTF-8");
                        + URLEncoder.encode(oauth.signature, "UTF-8");
//            +oauth.signature;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                url = null;
            }
            Log.v("URL ", url);
            Log.v("SINGNATURE ", oauth.signature);

            getDataFromWeb_Get.getData(this, this, new String[]{"http://localhost/wordpress/wc-api/v3/products/", url});

        }
    }
