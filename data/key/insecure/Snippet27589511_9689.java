public class Woocommerce {

private static String key = "ck_c6f7ea09138c------";
private static String secret = "cs_ed5c525f563dff-----";

private static final String HMAC_SHA1 = "HmacSHA1";

private static final String ENC = "UTF-8";

private static Base64 base64 = new Base64();

private static String getSignature(String url, String params)
        throws UnsupportedEncodingException, NoSuchAlgorithmException,
        InvalidKeyException {

    StringBuilder base = new StringBuilder();
    base.append("GET&");
    base.append(url);
    base.append("&");
    base.append(params);
    System.out.println("creating signature..." + base);

    byte[] keyBytes = (secret + "&").getBytes(ENC);

    SecretKey key = new SecretKeySpec(keyBytes, HMAC_SHA1);

    Mac mac = Mac.getInstance(HMAC_SHA1);
    mac.init(key);

    return new String(base64.encode(mac.doFinal(base.toString().getBytes(
            ENC))), ENC).trim();
}

public static void main(String[] args) throws ClientProtocolException,
        IOException, URISyntaxException, InvalidKeyException,
        NoSuchAlgorithmException {

    HttpClient httpclient = new DefaultHttpClient();
    List<NameValuePair> qparams = new ArrayList<NameValuePair>();
    // These params should ordered in key

    qparams.add(new BasicNameValuePair("oauth_consumer_key", key));
    qparams.add(new BasicNameValuePair("oauth_nonce", ""
            + (int) (Math.random() * 100000000)));
    qparams.add(new BasicNameValuePair("oauth_signature_method",
            "HMAC-SHA1"));
    qparams.add(new BasicNameValuePair("oauth_timestamp", ""
            + (System.currentTimeMillis() / 1000)));

    // generate the oauth_signature

    String signature = getSignature(
            URLEncoder.encode("www.buhoplace.net", ENC),
            URLEncoder.encode(URLEncodedUtils.format(qparams, ENC), ENC));
    // add it to params list
    qparams.add(new BasicNameValuePair("oauth_signature", signature));

    // generate URI which lead to access_token and token_secret.
    URI uri = URIUtils.createURI("http", "www.buhoplace.net", -1,
            "/wc-api/v2/products", URLEncodedUtils.format(qparams, ENC),
            null);

    System.out.println("url llamada" + uri);

    HttpGet httpget = new HttpGet(uri);
    // output the response content.
    System.out.println("Resultado de la llamada");

    HttpResponse response = httpclient.execute(httpget);
    HttpEntity entity = response.getEntity();
    if (entity != null) {
        InputStream instream = entity.getContent();
        int len;
        byte[] tmp = new byte[2048];
        while ((len = instream.read(tmp)) != -1) {
            System.out.println(new String(tmp, 0, len, ENC));
        }
    }
}
