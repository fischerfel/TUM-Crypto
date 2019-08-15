public class PrivateRequest extends Request<JSONObject> {
    private Mac shaMac;
    private SecretKeySpec keySpec;
    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;
    private Map<String, String> headers;

    private String nonce;
    private String sign;

    public PrivateRequest(int method, String url, Map<String, String> params, Response.ErrorListener errorListener) throws NullPointerException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        super(method, url, errorListener);
        this.nonce = String.valueOf(System.currentTimeMillis());
        this.params = params;
        this.shaMac = Mac.getInstance("HmacSHA512");
        keySpec = new SecretKeySpec("Secret".getBytes(), "HmacSHA512");
        shaMac.init(keySpec);

        final byte[] data = shaMac.doFinal(new JSONObject(params).toString().getBytes());
        sign = Base64.encodeToString(data, Base64.NO_WRAP);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> map = new HashMap<>();
        map.put("Key", "Public");
        map.put("Sign", sign);
        return map;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject jsonObject) {
        Log.d("PrivateRequest", jsonObject.toString());
    }
}
