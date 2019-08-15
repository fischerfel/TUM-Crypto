        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] sigBytes = md5.digest((sharedSecret+"api_key"+API_KEY).getBytes());
        api_sig = new BigInteger(sigBytes).toString(16);
