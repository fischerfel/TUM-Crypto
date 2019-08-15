class BackgroundTasks extends AsyncTask<Void, Void, Void> {
    Context ctx;
    public static final String CONSUMER_KEY = "ck_49885988eurue8343883ue82w27w";
    public static final String CONSUMER_SECRET = "cs_398ewwijdsjssuiuewoooww";
    public final String TOKEN = "34reeqe3rghytnhddccc.ya89198ajsM8jsxaw83I9xkamb9bCikzkkakasMNS9wakjaJA9wjiajakmmazajjjx.duiiu38hs32uwjusdwsdes";

    BackgroundTasks(Context ctx) {
        this.ctx = ctx;
    }
    @Override
    protected Void doInBackground(Void...voids) {

        HttpClient httpclient = new DefaultHttpClient();
        String url1 = "http://www.test.com/wp-json/wc/v1/orders/9974";
        String oauth_signature_method;
        oauth_signature_method = "HMAC-SHA1";
        ArrayList<NameValuePair> params;
        RequestPackage p = new RequestPackage();
        p.setMethod("POST");
        p.setUri(url1);
        p.setParm("oauth_consumer_key",CONSUMER_KEY);
        p.setParm("oauth_signature_method",oauth_signature_method);
        p.setParm("oauth_token",TOKEN);
        p.setParm("oauth_timestamp",generateTimestamp());
        p.setParm("oauth_nonce",generateNonce());
        p.setParm("oauth_version","1.0");
        String string_to_sign = BuildConfig.VERSION_NAME;
        try {
            string_to_sign = "POST&" + URLEncoder.encode(url1, "utf-8") + "&" + p.getEncodedParams();
        } catch (UnsupportedEncodingException e3) {
            e3.printStackTrace();
        }
        try {
            Mac mac = Mac.getInstance("HMAC-SHA1");
            mac.init(new SecretKeySpec(CONSUMER_SECRET.getBytes("utf-8"), "HMAC-SHA1"));
            String signature = Base64.encodeToString(mac.doFinal(string_to_sign.getBytes("utf-8")), 0).trim();
            Log.d("signature", signature);
            p.setParm("oauth_signature", signature);
        } catch (NoSuchAlgorithmException e4) {
            e4.printStackTrace();
        } catch (InvalidKeyException e5) {
            e5.printStackTrace();
        } catch (UnsupportedEncodingException e6) {
            e6.printStackTrace();
        }
        String reqUrl = p.getUri();
        reqUrl+="?"+p.getEncodedParams();

        HttpPost httppost = new HttpPost(reqUrl);
        try {
            Gson gson=new Gson();
            order order = new order();
            order.setPaymentMethod("cod");
            order.setPaymentMethodTitle("Cash on delivery");
            order.setBilling(user.get(0).getBilling());
            order.setShipping(user.get(0).getShipping());
            order.setCustomerId(user.get(0).getId());

            ArrayList<com.example.priyankas.restapivendorapp.order.LineItem> list_lineitems=new ArrayList<com.example.priyankas.restapivendorapp.order.LineItem>();
            for (int j = 0; j < cartSize; j++) {
                order.LineItem lineItem = new order.LineItem();
                lineItem.setProductId(cart_items.get(j).getId());
                lineItem.setQuantity(cart_items.get(j).quantity);
                lineItem.setVariationId(0);
                list_lineitems.add(lineItem);
            }
            order.setLineItems(list_lineitems);
            String json_order=gson.toJson(order);
            StringEntity stringEntity = new StringEntity(json_order);
            httppost.setEntity(stringEntity);
            httppost.setHeader("Content-Type", "application/json");
            httppost.addHeader("Accept", "application/json");
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            Log.e("mainToPost", "gfgfffg" + response.toString());
            InputStream inputStream = response.getEntity().getContent();
            InputStreamToStringExample str = new InputStreamToStringExample();
            responseServer = str.getStringFromInputStream(inputStream);
            Log.d("Full url is  ",String.valueOf(reqUrl));
            Log.e("response", "response -----" + responseServer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
protected String generateTimestamp() {
    return Long.toString(System.currentTimeMillis() / 1000L);
}

protected String generateNonce() {
    return Long.toString(random.nextLong());
}


public class RequestPackage {
    private String uri;
    private  String method = "POST";
    private Map<String,String> params = new HashMap<>();


    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
    public void setParm(String key, String value){
        params.put(key,value);
    }
    public String getEncodedParams(){
        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()){
            String value = null;
            try {
                value = URLEncoder.encode(params.get(key),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (sb.length()>0){
                sb.append("&");
            }
            sb.append(key + "=" + value);
        }
        return sb.toString();
    }
}
