public class PoloTest {

  public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, ClientProtocolException, IOException {

    String key = "YOUR API KEY HERE";
    String secret = "YOUR API SECRET HERE";
    String url = "https://poloniex.com/tradingApi";
    String nonce = String.valueOf(System.currentTimeMillis());
    String queryArgs = "command=returnBalances&nonce=" + nonce;

    Mac shaMac = Mac.getInstance("HmacSHA512");
    SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA512");
    shaMac.init(keySpec);
    final byte[] macData = shaMac.doFinal(queryArgs.getBytes());
    String sign = Hex.encodeHexString(macData);

    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpPost post = new HttpPost(url);
    post.addHeader("Key", key); 
    post.addHeader("Sign", sign);

    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("command", "returnBalances"));
    params.add(new BasicNameValuePair("nonce", nonce));
    post.setEntity(new UrlEncodedFormEntity(params));

    CloseableHttpResponse response = httpClient.execute(post);
    HttpEntity responseEntity = response.getEntity();
    System.out.println(response.getStatusLine());
    System.out.println(EntityUtils.toString(responseEntity));
  }

}
