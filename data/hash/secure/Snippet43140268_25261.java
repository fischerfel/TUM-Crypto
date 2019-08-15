private void generateChecksum(String merchantKey) {


    JSONObject jsonObject = new JSONObject();


    checksumObj = new ChecksumObj(mEdtAmount.getText().toString(), "ANDROID", mEdtEmail.getText().toString(),
            "https://www.google.com", "merchantID", getTransactionID(), mEdtMobile.getText().toString(), "auth", "https://www.google.com");


    try {
        jsonObject.put("amount", checksumObj.getAmount());
        jsonObject.put("channel", checksumObj.getChannel());
        jsonObject.put("email", checksumObj.getEmail());
        jsonObject.put("furl", checksumObj.getFurl());
        jsonObject.put("merchantId", checksumObj.getMerchantId());
        jsonObject.put("merchantTxnId", checksumObj.getMerchantTxnId());
        jsonObject.put("mobile", checksumObj.getMobile());
        jsonObject.put("productInfo", checksumObj.getProductInfo());
        jsonObject.put("surl", checksumObj.getSurl());

    } catch (JSONException e) {
        e.printStackTrace();
    }

    String plainText = jsonObject.toString().concat(merchantKey).replace("\\/", "/");

    MessageDigest md = null;

    try {
        md = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    md.update(plainText.getBytes(Charset.defaultCharset()));
    byte[] mdbytes = md.digest();

    // convert the byte to hex format method 1

    StringBuffer checksum = new StringBuffer();
    for (int i = 0; i < mdbytes.length; i++) {
        checksum.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
    }

    jsonCheckSum = checksum.toString();

    requestMap();



}
