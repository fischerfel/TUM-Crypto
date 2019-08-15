String result;

try {
        String data = "mydata";
        String key = "myKey";
        // Get an hmac_sha1 key from the raw key bytes
        byte[] keyBytes = key.getBytes();
        SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");

        // Get an hmac_sha1 Mac instance and initialize with the signing key
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signingKey);

        // Compute the hmac on input data bytes
        byte[] rawHmac = mac.doFinal(data.getBytes());

        // Convert raw bytes to Hex
        byte[] hexBytes = new Hex().encode(rawHmac);

        //  Covert array of Hex bytes to a String
        result = new String(hexBytes, "ISO-8859-1");
        out.println("MAC : " + result);
}
catch (Exception e) {

}
