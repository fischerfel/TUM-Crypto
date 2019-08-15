public static String constructOTP(final Long counter, final String key) 
    throws NoSuchAlgorithmException, DecoderException, InvalidKeyException 
{ 
    // setup the HMAC algorithm, setting the key to use         
    final Mac mac = Mac.getInstance("HmacSHA512");                  

    // convert the key from a hex string to a byte array         
    final byte[] binaryKey = Hex.decodeHex(key.toCharArray());                  

    // initialize the HMAC with a key spec created from the key         
    mac.init(new SecretKeySpec(binaryKey, "HmacSHA512"));  

    // compute the OTP using the bytes of the counter         
    byte[] computedOtp = mac.doFinal(                 
    ByteBuffer.allocate(8).putLong(counter).array());  

    //         
    // increment the counter and store the new value         
    //                  

    // return the value as a hex encoded string         
    return new String(Hex.encodeHex(computedOtp));     
} 
