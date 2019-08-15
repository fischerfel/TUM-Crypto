    byte[] input = null;
    try {
        input = new String("X-Session-Id" + tmpSessionId + "test").getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        System.out.println("Unsupported encoding exception");
        e.printStackTrace();
    }

    MessageDigest messageDigest;
    byte[] output = new byte[32]; // since SHA256 hash is 256 bits = 32 bytes
    try {
        messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(input, 0, input.length);
        messageDigest.digest(output, 0, 32); // same
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (DigestException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
