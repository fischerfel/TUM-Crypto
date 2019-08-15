private static String printMacAsBase64(byte[] macKey, String counter) throws Exception {
    // import AES 128 MAC_KEY
    SecretKeySpec signingKey = new SecretKeySpec(macKey, "AES");

    // create new HMAC object with SHA-256 as the hashing algorithm
    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(signingKey);

    // integer -> string -> bytes -> encrypted bytes
    byte[] counterMac = mac.doFinal(counter.getBytes("UTF-8"));

    // base 64 encoded string
    return DatatypeConverter.printBase64Binary(counterMac);
}
