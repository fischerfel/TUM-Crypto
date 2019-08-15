public static String POLONIEX_SECRET_KEY = "my secret"; //KEY
public static String POLONIEX_API_KEY = "my key"; // TODO API KEY


public static void main(String[] args) {

    try {
        accessPoloniex();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

}
public static final long generateNonce(){

    Date d = new Date();
    return d.getTime();
}

public static final void accessPoloniex() throws IOException{

    String nonce = new BigDecimal(Polo2.generateNonce()).toString();

    String connectionString = "https://poloniex.com/tradingApi";

    String queryArgs = "command=returnBalances";

    String hmac512 = hmac512Digest(queryArgs, POLONIEX_SECRET_KEY);

    // Produce the output
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Writer writer = new OutputStreamWriter(out, "UTF-8");
    writer.append(queryArgs);


    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpPost post = new HttpPost(connectionString);

    post.setHeader("Key", POLONIEX_API_KEY);
    post.setHeader("Sign", hmac512);

    post.setEntity(new ByteArrayEntity(out.toByteArray()));
    List<NameValuePair> params = new ArrayList<>();
    params.add(new BasicNameValuePair("nonce", nonce));

    CloseableHttpResponse response = null;
    Scanner in = null;
    try
    {
        post.setEntity(new UrlEncodedFormEntity(params));
        response = httpClient.execute(post);
        // System.out.println(response.getStatusLine());
        HttpEntity entity = response.getEntity();
        in = new Scanner(entity.getContent());
        while (in.hasNext())
        {
            System.out.println(in.next());

        }
        EntityUtils.consume(entity);
    } finally
    {
        in.close();
        response.close();
    }

}

public static String hmac512Digest(String msg, String keyString) {

    Mac shaMac;
    try {
        shaMac = Mac.getInstance("HmacSHA512");
        SecretKeySpec  keySpec = new SecretKeySpec(keyString.getBytes(), "HmacSHA512");

        shaMac.init(keySpec);
        final byte[] macData = shaMac.doFinal(msg.getBytes());
        return Hex.encodeHexString(macData); //again with try/catch for InvalidKeyException

    } catch (Exception e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    } 
    return null;
}
