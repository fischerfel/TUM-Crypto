//creating the timestamp
    long timestamp = System.currentTimeMillis() / 1000;
    //getting the int value of it
    int value = (int) timestamp;

    //just a string that is the value of the hmac
    String input = String.valueOf(value) + "/" + url;
    //new hmac instance
    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
    //my secret key where bytes is "my key".getBytes();
    SecretKeySpec secret_key = new SecretKeySpec(bytes, "HmacSHA256");
    sha256_HMAC.init(secret_key);
    //trying to have as string
    String txt2 = toHexString (sha256_HMAC.doFinal(input.getBytes()));
