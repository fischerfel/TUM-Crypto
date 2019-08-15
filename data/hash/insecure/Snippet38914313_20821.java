    byte[] rawSHA = null;
    byte[] base64HexSHA = null;
    MessageDigest md= null;

    try {
        md = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
        LOG.error("Unable to load MD5 Message Digest : " + e.getMessage(), e);
        throw new IllegalStateException("MD5 Message Digest Instance Not Found");
    }


    rawSHA = md.digest(rawText.getBytes("UTF-8"));
    base64HexSHA = Base64.encodeBase64(rawSHA);
    System.out.println("result = "+base64HexSHA );
