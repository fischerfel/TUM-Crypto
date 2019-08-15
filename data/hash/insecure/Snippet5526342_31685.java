private final void processChallenge(XmlPullParser parser, Writer writer,
        String sessionKey, String sessionSecret) throws IOException,
        NoSuchAlgorithmException, XmlPullParserException {

    parser.require(XmlPullParser.START_TAG, null, "challenge");
    String challenge = new String(Base64.decode(parser.nextText(),
            Base64.DEFAULT));

    String params[] = challenge.split("&");
    HashMap<String, String> paramMap = new HashMap<String, String>();
    for (int i = 0; i < params.length; ++i) {
        String p[] = params[i].split("=");
        p[0] = URLDecoder.decode(p[0]);
        p[1] = URLDecoder.decode(p[1]);
        paramMap.put(p[0], p[1]);
    }

    String api_key = "YOUR_API_KEY";
    String call_id = "" + System.currentTimeMillis();
    String method = paramMap.get("method");
    String nonce = paramMap.get("nonce");
    String v = "1.0";

    StringBuffer sigBuffer = new StringBuffer();
    sigBuffer.append("api_key=" + api_key);
    sigBuffer.append("call_id=" + call_id);
    sigBuffer.append("method=" + method);
    sigBuffer.append("nonce=" + nonce);
    sigBuffer.append("session_key=" + sessionKey);
    sigBuffer.append("v=" + v);
    sigBuffer.append(sessionSecret);

    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(sigBuffer.toString().getBytes());
    byte[] digest = md.digest();

    StringBuffer sig = new StringBuffer();
    for (int i = 0; i < digest.length; ++i) {
        sig.append(Integer.toHexString(0xFF & digest[i]));
    }

    StringBuffer response = new StringBuffer();
    response.append("api_key=" + URLEncoder.encode(api_key));
    response.append("&call_id=" + URLEncoder.encode(call_id));
    response.append("&method=" + URLEncoder.encode(method));
    response.append("&nonce=" + URLEncoder.encode(nonce));
    response.append("&session_key=" + URLEncoder.encode(sessionKey));
    response.append("&v=" + URLEncoder.encode(v));
    response.append("&sig=" + URLEncoder.encode(sig.toString()));

    StringBuilder out = new StringBuilder();
    out.append("<response xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>");
    out.append(Base64.encodeToString(response.toString().getBytes(),
            Base64.NO_WRAP));
    out.append("</response>");

    writer.write(out.toString());
    writer.flush();
}
