    MessageDigest digest;
    try {
        digest = MessageDigest.getInstance("MD5");
        byte utf8_bytes[] = tag_xml.getBytes();
        digest.update(utf8_bytes,0,utf8_bytes.length);
        hash = new BigInteger(1, digest.digest()).toString(16);
    } 
    catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
