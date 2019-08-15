@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String HMAC_ALGORITHM = "HmacSHA256";
    resp.setContentType("text/html;charset=UTF-8");
    Map<String,String[]> parameters = req.getParameterMap();
    String data = null;
    SortedSet<String> keys = new TreeSet<String>(parameters.keySet());
    for (String key : keys) {
        if (!key.equals("hmac")&&!key.equals("signature")){
        if (data == null){
            data = key + "=" +req.getParameter(key);
        }
            else {
            data = data + "&" + key + "=" + req.getParameter(key);
        }
    }
    }
    SecretKeySpec keySpec = new SecretKeySpec(SHARED_KEY.getBytes(),HMAC_ALGORITHM);
    Mac mac = null;
    try {
        mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(keySpec);
        byte[] rawHmac = mac.doFinal(data.getBytes());
        if (Hex.encodeHexString(rawHmac).equals(req.getParameter("hmac"))){
            //THE HMAC IS VERIFIED
        } else {
            //THE HMAC IS NOT VERIFIED
        }
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
        e.printStackTrace();
    }
}
